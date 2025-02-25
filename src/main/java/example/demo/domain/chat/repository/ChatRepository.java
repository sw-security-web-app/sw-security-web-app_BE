package example.demo.domain.chat.repository;

import example.demo.domain.chat.Chat;
import example.demo.domain.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long>, ChatRepositoryCustom {
    List<Chat> findByChatRoom(ChatRoom chatRoom);
}
