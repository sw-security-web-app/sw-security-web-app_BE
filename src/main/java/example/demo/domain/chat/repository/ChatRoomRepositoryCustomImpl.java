package example.demo.domain.chat.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import example.demo.domain.chat.AIModelType;
import example.demo.domain.chat.ChatRoom;
import example.demo.domain.chat.QChatRoom;
import example.demo.domain.chat.dto.request.ChatRoomRequestDto;
import example.demo.domain.chat.dto.request.QChatRoomRequestDto;
import example.demo.domain.chat.dto.response.ChatRoomRecentResponseDto;

import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.stream.Collectors;

import static example.demo.domain.chat.QChat.chat;
import static example.demo.domain.chat.QChatRoom.*;

public class ChatRoomRepositoryCustomImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ChatRoomRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ChatRoomRecentResponseDto> findLatestChatRoomWithLatestAnswer(Long memberId, AIModelType aiModelType) {
        List<Long> latestChatRoomIds = getLatestChatRoomIds(memberId, aiModelType);

        return latestChatRoomIds.stream()
                .map(this::getChatRoomRecentResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChatRoomRequestDto> findByMemberOrderByCreatedAtAsc(Long memberId) {
        return  queryFactory
                .select(new QChatRoomRequestDto(
                        chatRoom.chatRoomId,
                        chatRoom.createdAt
                        )
                )
                .from(chatRoom)
                .leftJoin(chat.chatRoom,chatRoom)
                .where(memberIdEq(memberId))
                .orderBy(chatRoom.createdAt.asc())
                .fetch();
    }

    @Override
    public List<ChatRoomRequestDto> findByMemberIdAndAiModelType(Long memberId, AIModelType aiModelType) {
        return queryFactory
                .select(new QChatRoomRequestDto(chatRoom.chatRoomId, chatRoom.createdAt))
                .from(chatRoom)
                .leftJoin(chat).on(chat.chatRoom.chatRoomId.eq(chatRoom.chatRoomId))
                .where(memberIdEq(memberId), aiModelTypeEq(aiModelType))
                .groupBy(chatRoom.chatRoomId)
                .orderBy(chatRoom.createdAt.desc())
                .fetch();
    }

    private BooleanExpression memberIdEq(Long memberId) {
        return memberId != null ? chatRoom.member.memberId.eq(memberId) : null;
    }

    private BooleanExpression aiModelTypeEq(AIModelType aiModelType) {
        return aiModelType != null ? chat.modelType.eq(aiModelType) : null;
    }

    private static BooleanExpression chatRoomIdEq(Long chatRoomId) {
        return chatRoomId != null ? chat.chatRoom.chatRoomId.eq(chatRoomId) : null;
    }

    private List<Long> getLatestChatRoomIds(Long memberId, AIModelType aiModelType) {
        return queryFactory
                .select(chat.chatRoom.chatRoomId)
                .from(chat)
                .where(memberIdEq(memberId), aiModelTypeEq(aiModelType))
                .groupBy(chat.chatRoom.chatRoomId)
                .orderBy(chat.chatRoom.createdAt.desc())
                .limit(4)
                .fetch();
    }

    private ChatRoomRecentResponseDto getChatRoomRecentResponseDto(Long chatRoomId) {
        String latestAnswer = getLatestAnswer(chatRoomId);
        return new ChatRoomRecentResponseDto(chatRoomId, latestAnswer);
    }

    private String getLatestAnswer(Long chatRoomId) {
        return queryFactory
                .select(chat.answer)
                .from(chat)
                .where(chatRoomIdEq(chatRoomId))
                .orderBy(chat.createdAt.desc())
                .limit(1)
                .fetchOne();
    }
}
