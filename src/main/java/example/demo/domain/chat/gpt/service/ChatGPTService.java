package example.demo.domain.chat.gpt.service;

import example.demo.domain.chat.gpt.dto.ChatCompletionDto;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * ChatGPT 서비스 인터페이스
 *
 * @Author : 박재성
 * @Since : 02/15/2025
 *
 */

@Service
public interface ChatGPTService {
    Map<String, Object> prompt(ChatCompletionDto chatCompletionDto);
}
