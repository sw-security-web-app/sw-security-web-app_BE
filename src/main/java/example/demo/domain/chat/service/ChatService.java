package example.demo.domain.chat.service;

import example.demo.domain.chat.AIModelType;
import example.demo.domain.chat.dto.request.ChatDto;
import example.demo.domain.chat.dto.response.ChatTotalDetailResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface ChatService {

    void saveChat(Long memberId, ChatDto chatDto, Long chatRoomId);

    ChatTotalDetailResponseDto getDetailChattingContent(Long chatRoomId, String token, Long chatId, int size, AIModelType type);
}
