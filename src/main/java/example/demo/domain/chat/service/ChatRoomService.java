package example.demo.domain.chat.service;

import example.demo.domain.chat.AIModelType;
import example.demo.domain.chat.dto.ChatRoomGetResponseDto;
import example.demo.domain.chat.dto.ChatRoomRecentResponseDto;
import example.demo.domain.chat.dto.ChatRoomResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChatRoomService {
    ChatRoomResponseDto createChatRoom(Long memberId);

    List<ChatRoomGetResponseDto> getChatRoomList(Long memberId, AIModelType aiModelType);

    List<ChatRoomRecentResponseDto> getLatestChatRoom(Long memberId);
}
