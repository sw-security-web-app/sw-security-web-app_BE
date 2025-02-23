package example.demo.domain.chat.service;

import example.demo.domain.chat.dto.ChatDto;
import example.demo.domain.chat.dto.ChatRoomResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChatRoomService {
    ChatRoomResponseDto createChatRoom(Long memberId);

    List<ChatDto> getChatListByChatRoomId(Long chatRoomId);
}
