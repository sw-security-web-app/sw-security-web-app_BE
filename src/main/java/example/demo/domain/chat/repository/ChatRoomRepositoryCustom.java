package example.demo.domain.chat.repository;

import example.demo.domain.chat.dto.response.ChatRoomRecentResponseDto;
import example.demo.domain.chat.dto.request.ChatRoomRequestDto;
import example.demo.domain.chat.AIModelType;

import java.util.List;

public interface ChatRoomRepositoryCustom {
    List<ChatRoomRecentResponseDto> findLatestChatRoomWithLatestAnswer(Long memberId, AIModelType aiModelType);

    List<ChatRoomRequestDto> findByMemberOrderByCreatedAtAsc(Long memberId);

    List<ChatRoomRequestDto> findByMemberIdAndAiModelType(Long memberId, AIModelType aiModelType);
}
