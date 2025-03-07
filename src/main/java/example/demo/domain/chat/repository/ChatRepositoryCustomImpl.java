package example.demo.domain.chat.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import example.demo.domain.chat.AIModelType;
import example.demo.domain.chat.dto.response.ChatDetailResponseDto;

import example.demo.domain.chat.dto.response.ChatTotalDetailResponseDto;
import example.demo.domain.chat.dto.response.QChatDetailResponseDto;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

import static example.demo.domain.chat.QChatRoom.chatRoom;

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
    public ChatTotalDetailResponseDto getSliceOfChatting(Long chatRoomId, Long chatId,Long memberId,int size,AIModelType type) {
        //채팅 상세 목록 조회
        List<ChatDetailResponseDto> chatDetailDtoList=queryFactory
                .select(new QChatDetailResponseDto(
                        chat.chatId,
                        chat.question,
                        chat.answer,
                        chat.createdAt
                ))
                .from(chat)
                .innerJoin(chat.chatRoom,chatRoom)
                .innerJoin(chatRoom.member,member)
                .where(allEq(chatRoomId,chatId,memberId,type))
                .orderBy(chat.chatId.desc())
                .limit(size)
                .fetch();

        // 결과가 비어 있는 경우
        if (chatDetailDtoList.isEmpty()) {
            return ChatTotalDetailResponseDto.builder()
                    .lastChatId(chatId)
                    .array(Collections.emptyList())
                    .build();
        }

        ChatTotalDetailResponseDto responseDto=ChatTotalDetailResponseDto
                .builder()
                .lastChatId(chatDetailDtoList.get(chatDetailDtoList.size()-1).getChatId())
                .array(chatDetailDtoList)
                .build();

        return responseDto;
    }

    private BooleanExpression memberIdEq(Long memberId){
        return memberId==null ? null : member.memberId.eq(memberId);
    }

    private BooleanExpression chatRoomIdEq(Long chatRoomId){
        return chatRoomId == null ? null : chatRoom.chatRoomId.eq(chatRoomId);
    }
    private BooleanExpression gtChatId(Long chatId){
        return (chatId != null && chatId > 0) ? chat.chatId.lt(chatId) : null;
    }
    private BooleanExpression aiTypeEq(AIModelType ai){
        return ai==null ? null : chat.modelType.eq(ai);
    }

    private BooleanExpression allEq(Long chatRoomId, Long chatId, Long memberId,AIModelType type) {
        BooleanExpression condition = chatRoomIdEq(chatRoomId);
        BooleanExpression chatCondition = gtChatId(chatId);
        BooleanExpression memberCondition = memberIdEq(memberId);
        BooleanExpression aiTypeCondition=aiTypeEq(type);

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
        if(aiTypeCondition!=null){
            return (result!=null) ? result.and(aiTypeCondition) : aiTypeCondition;
        }

        return result;
    }


}
