package example.demo.domain.chat.service;

import example.demo.domain.chat.dto.response.ChatRoomRecentResponseDto;
import example.demo.domain.chat.dto.response.ChatRoomResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChatRoomService {
    ChatRoomResponseDto createChatRoom(Long memberId);

    List<ChatRoomRecentResponseDto> getLatestChatRoom(Long memberId);
}
