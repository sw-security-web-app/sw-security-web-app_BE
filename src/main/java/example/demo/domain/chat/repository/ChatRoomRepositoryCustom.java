package example.demo.domain.chat.repository;

import example.demo.domain.chat.dto.ChatRoomRecentResponseDto;

import java.util.List;

public interface ChatRoomRepositoryCustom {
    List<ChatRoomRecentResponseDto> findLatestChatRoomWithLatestAnswer(Long memberId);
}
