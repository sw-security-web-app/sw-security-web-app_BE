package example.demo.domain.chat.gemini.service;

import org.springframework.stereotype.Service;

/**
 * Gemini Service Interface
 *
 * @Author : 박재성
 * @Since : 02/16/2025
 */

@Service
public interface QnAService {
    String getAnswer(String question);
}
