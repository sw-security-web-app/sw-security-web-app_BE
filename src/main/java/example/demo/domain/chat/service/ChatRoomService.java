package example.demo.domain.chat.service;

import example.demo.domain.chat.dto.ChatRoomResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface ChatRoomService {
    ChatRoomResponseDto createChatRoom(Long memberId);
}
