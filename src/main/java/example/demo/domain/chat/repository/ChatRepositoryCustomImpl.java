package example.demo.domain.chat.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import example.demo.domain.chat.Chat;
import example.demo.domain.chat.QChat;
import example.demo.domain.chat.QChatRoom;
import example.demo.domain.chat.dto.ChatDetailDto;
import example.demo.domain.chat.dto.QChatDetailDto;
import example.demo.domain.chat.dto.QChatDto;
import example.demo.domain.company.dto.response.QCompanyResponseDto;
import example.demo.domain.member.QMember;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static example.demo.domain.chat.QChat.chat;
import static example.demo.domain.chat.QChatRoom.*;
import static example.demo.domain.member.QMember.*;
@Repository
public class ChatRepositoryCustomImpl implements ChatRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ChatRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public List<ChatDetailDto> getSliceOfChatting(Long chatRoomId, Long chatId,Long memberId,int size) {
        List<ChatDetailDto> chatDetailDtoList=queryFactory
                .select(new QChatDetailDto(
                        chat.chatId,
                        chat.question,
                        chat.answer,
                        chat.createdAt
                ))
                .from(chat)
                .innerJoin(chat.chatRoom,chatRoom)
                .innerJoin(chatRoom.member,member)
                .where(allEq(chatRoomId,chatId,memberId))
                .orderBy(chat.chatId.desc())
                .limit(size)
                .fetch();

        return chatDetailDtoList.isEmpty() ? Collections.emptyList() : chatDetailDtoList;
    }

    private BooleanExpression memberIdEq(Long memberId){
        return memberId==null ? null : member.memberId.eq(memberId);
    }

    private BooleanExpression chatRoomIdEq(Long chatRoomId){
        return chatRoomId == null ? null : chatRoom.chatRoomId.eq(chatRoomId);
    }
    private BooleanExpression gtChatId(Long chatId){
        return chatId == null ? null : chat.chatId.gt(chatId);
    }

    private BooleanExpression allEq(Long chatRoomId, Long chatId, Long memberId) {
        BooleanExpression condition = chatRoomIdEq(chatRoomId);
        BooleanExpression chatCondition = gtChatId(chatId);
        BooleanExpression memberCondition = memberIdEq(memberId);

        BooleanExpression result = null;

        if (condition != null) {
            result = condition;
        }
        if (chatCondition != null) {
            result = (result != null) ? result.and(chatCondition) : chatCondition;
        }
        if (memberCondition != null) {
            result = (result != null) ? result.and(memberCondition) : memberCondition;
        }

        return result;
    }


}
