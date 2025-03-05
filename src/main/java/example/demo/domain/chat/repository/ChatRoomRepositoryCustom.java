package example.demo.domain.chat.repository;

import example.demo.domain.chat.dto.response.ChatRoomRecentResponseDto;
import example.demo.domain.chat.dto.request.ChatRoomRequestDto;

import java.util.List;

public interface ChatRoomRepositoryCustom {
    List<ChatRoomRecentResponseDto> findLatestChatRoomWithLatestAnswer(Long memberId);

    List<ChatRoomRequestDto> findByMemberOrderByCreatedAtAsc(Long memberId);
}
