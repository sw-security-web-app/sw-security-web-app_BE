package example.demo.domain.chat.repository;

import example.demo.domain.chat.Chat;

import java.util.List;

public interface ChatRepositoryCustom {
    List<Chat> findChatListByChatRoomId(Long chatRoomId);
}
