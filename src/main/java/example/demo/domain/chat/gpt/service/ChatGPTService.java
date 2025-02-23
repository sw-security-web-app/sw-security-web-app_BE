package example.demo.domain.chat.gpt.service;

import example.demo.domain.chat.gpt.dto.ChatGPTRequestDto;
import example.demo.domain.chat.gpt.dto.ChatGPTResponseDto;
import org.springframework.stereotype.Service;

/**
 * ChatGPT 서비스 인터페이스
 *
 * @Author : 박재성
 * @Since : 02/15/2025
 *
 */

@Service
public interface ChatGPTService {
    ChatGPTResponseDto prompt(ChatGPTRequestDto requestDto, Long memberId);
}
