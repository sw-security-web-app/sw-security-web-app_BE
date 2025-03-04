package example.demo.domain.chat.service;

import example.demo.domain.chat.dto.ChatDetailDto;
import example.demo.domain.chat.dto.ChatDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChatService {

    void saveChat(Long memberId, ChatDto chatDto, Long chatRoomId);

    List<ChatDetailDto> getDetailChattingContent(Long chatRoomId,String token,Long chatId,int size);
}
