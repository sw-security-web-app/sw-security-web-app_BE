package example.demo.domain.chat.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import example.demo.domain.chat.AIModelType;
import example.demo.domain.chat.ChatRoom;
import example.demo.domain.chat.QChatRoom;
import example.demo.domain.chat.dto.request.ChatRoomRequestDto;
import example.demo.domain.chat.dto.request.QChatRoomRequestDto;
import example.demo.domain.chat.dto.response.ChatRoomGetResponseDto;
import example.demo.domain.chat.dto.response.ChatRoomRecentResponseDto;
import static com.querydsl.core.types.dsl.Expressions.stringTemplate;
import example.demo.domain.chat.dto.response.QChatRoomGetResponseDto;
import example.demo.domain.chat.dto.response.QChatRoomRecentResponseDto;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;
import static example.demo.domain.chat.QChat.chat;
import static example.demo.domain.chat.QChatRoom.*;

public class ChatRoomRepositoryCustomImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ChatRoomRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ChatRoomRecentResponseDto> findLatestChatRoomWithLatestAnswer(Long memberId, AIModelType aiModelType) {
        return queryFactory
                .select(new QChatRoomRecentResponseDto(
                        chat.chatRoom.chatRoomId,
                        chat.answer,
                        chat.createdAt
                ))
                .from(chat)
                .where(memberIdEq(memberId), aiModelTypeEq(aiModelType), chat.chatId.in(
                        select(chat.chatId.max())
                                .from(chat)
                                .where(memberIdEq(memberId), aiModelTypeEq(aiModelType))
                                .groupBy(chat.chatRoom.chatRoomId)
                ))
                .orderBy(chat.createdAt.desc())
                .limit(4)
                .fetch();
    }

    @Override
    public List<ChatRoomGetResponseDto> findByMemberIdAndAiModelType(Long memberId, AIModelType aiModelType) {
        return queryFactory
                .select(new QChatRoomGetResponseDto(
                        chatRoom.chatRoomId,
                        stringTemplate("CAST({0} AS STRING)",chat.question).substring(0,10),
                        chatRoom.createdAt
                ))
                .from(chatRoom)
                .leftJoin(chat).on(chat.chatRoom.chatRoomId.eq(chatRoom.chatRoomId))
                .where(memberIdEq(memberId), aiModelTypeEq(aiModelType), chat.chatId.in(
                        select(chat.chatId.max())
                                .from(chat)
                                .where(memberIdEq(memberId), aiModelTypeEq(aiModelType))
                                .groupBy(chat.chatRoom.chatRoomId)
                ))
                .orderBy(chatRoom.createdAt.desc())
                .fetch();
    }

    private BooleanExpression memberIdEq(Long memberId) {
        return memberId != null ? chatRoom.member.memberId.eq(memberId) : null;
    }

    private BooleanExpression aiModelTypeEq(AIModelType aiModelType) {
        return aiModelType != null ? chat.modelType.eq(aiModelType) : null;
    }

}
