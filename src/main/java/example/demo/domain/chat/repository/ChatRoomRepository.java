package example.demo.domain.chat.repository;

import example.demo.domain.chat.AIModelType;
import example.demo.domain.chat.ChatRoom;
import example.demo.domain.chat.dto.response.ChatRoomGetResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom {
    List<ChatRoomGetResponseDto> findByMemberIdAndAiModelType(Long memberId, AIModelType aiModelType);

    @Query(
            nativeQuery = true,
            value = "select distinct r.chat_room_id " +
                    "from chat_room as r " +
                    "inner join chat as c on c.chat_room_id = r.chat_room_id " +
                    "where c.member_id = :memberId and c.model_type = :aiType"
    )
    List<Long> getChatRoomByChatRoomId(@Param(value = "memberId")Long memberId,@Param(value = "aiType") AIModelType aiType);

}
