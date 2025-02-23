package example.demo.domain.chat.repository;

import example.demo.domain.chat.Chat;

import java.util.Optional;

public interface ChatRepositoryCustom {
    Optional<Chat> findChatListByChatRoomId(Long chatRoomId);
}
