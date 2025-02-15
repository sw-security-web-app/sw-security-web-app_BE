package example.demo.domain.chat.gpt.service;

import example.demo.domain.chat.gpt.dto.ChatCompletionDto;
import example.demo.domain.chat.gpt.dto.CompletionDto;
import org.springframework.stereotype.Service;

import java.util.List;
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

    List<Map<String, Object>> modelList();

    Map<String, Object> isValidModel(String modelName);

    Map<String, Object> legacyPrompt(CompletionDto completionDto);

    Map<String, Object> prompt(ChatCompletionDto chatCompletionDto);
}
