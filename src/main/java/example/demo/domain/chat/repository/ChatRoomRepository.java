package example.demo.domain.chat.repository;

import example.demo.domain.chat.AIModelType;
import example.demo.domain.chat.ChatRoom;
import example.demo.domain.chat.dto.ChatRoomRequestDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom {
    List<ChatRoomRequestDto> findByMemberIdAndAiModelType(Long memberId, AIModelType aiModelType);
}
