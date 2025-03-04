package example.demo.domain.chat.service;

import example.demo.domain.chat.dto.ChatDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChatService {

    void saveChat(Long memberId, ChatDto chatDto, Long chatRoomId);

    List<>
}
