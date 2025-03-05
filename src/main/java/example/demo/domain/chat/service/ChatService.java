package example.demo.domain.chat.service;

import example.demo.domain.chat.AIModelType;
import example.demo.domain.chat.dto.response.ChatDetailResponseDto;
import example.demo.domain.chat.dto.ChatDto;
import example.demo.domain.chat.dto.response.ChatTotalDetailResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChatService {

    void saveChat(Long memberId, ChatDto chatDto, Long chatRoomId);

    ChatTotalDetailResponseDto getDetailChattingContent(Long chatRoomId, String token, Long chatId, int size, AIModelType type);
}
