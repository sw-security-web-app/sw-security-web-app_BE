package example.demo.domain.chat.gemini.service;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Gemini Service Interface
 *
 * @Author : 박재성
 * @Since : 02/16/2025
 */

@Service
public interface QnAService {
    Map<String, Object> getAnswer(String prompt);
}
