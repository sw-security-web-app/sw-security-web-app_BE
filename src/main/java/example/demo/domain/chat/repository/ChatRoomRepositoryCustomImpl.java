package example.demo.domain.chat.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import example.demo.domain.chat.QChatRoom;
import example.demo.domain.chat.dto.QChatRoomResponseDto;
import jakarta.persistence.EntityManager;

import static example.demo.domain.chat.QChat.chat;
import static example.demo.domain.chat.QChatRoom.*;

public class ChatRoomRepositoryCustomImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ChatRoomRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

}
