package example.demo.domain.chat.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import example.demo.domain.chat.Chat;
import example.demo.domain.chat.QChat;
import example.demo.domain.chat.dto.QChatDto;
import jakarta.persistence.EntityManager;

import java.util.List;

import static example.demo.domain.chat.QChat.chat;

public class ChatRepositoryCustomImpl implements ChatRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ChatRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Chat> findChatListByChatRoomId(Long chatRoomId) {
        return queryFactory
                .selectFrom(chat)
                .where(chat.chatRoom.chatRoomId.eq(chatRoomId))
                .orderBy(chat.createdAt.desc())
                .fetch();
    }

}
