package example.demo.domain.chat.repository;

import example.demo.domain.chat.AIModelType;
import example.demo.domain.chat.dto.ChatRoomGetResponseDto;
import example.demo.domain.chat.dto.ChatRoomRecentResponseDto;

import java.util.List;

public interface ChatRoomRepositoryCustom {
    List<ChatRoomRecentResponseDto> findLatestChatRoomWithLatestAnswer(Long memberId, AIModelType aiModelType);

    List<ChatRoomGetResponseDto> findByMemberOrderByCreatedAtAsc(Long memberId);

    List<ChatRoomGetResponseDto> findByMemberIdAndAiModelType(Long memberId, AIModelType aiModelType);
}
