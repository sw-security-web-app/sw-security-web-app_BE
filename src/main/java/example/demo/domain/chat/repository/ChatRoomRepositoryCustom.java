package example.demo.domain.chat.repository;

import example.demo.domain.chat.dto.response.ChatRoomGetResponseDto;
import example.demo.domain.chat.dto.response.ChatRoomRecentResponseDto;
import example.demo.domain.chat.dto.request.ChatRoomRequestDto;
import example.demo.domain.chat.AIModelType;

import java.util.List;

public interface ChatRoomRepositoryCustom {
    List<ChatRoomRecentResponseDto> findLatestChatRoomWithLatestAnswer(Long memberId, AIModelType aiModelType);

    List<ChatRoomGetResponseDto> findByMemberIdAndAiModelType(Long memberId, AIModelType aiModelType);
}
