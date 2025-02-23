package example.demo.domain.chat.gemini.service;

import example.demo.domain.chat.gemini.dto.GeminiRequestDto;
import example.demo.domain.chat.gemini.dto.GeminiResponseDto;
import org.springframework.stereotype.Service;

/**
 * Gemini Service Interface
 *
 * @Author : 박재성
 * @Since : 02/16/2025
 */

@Service
public interface GeminiService {
    GeminiResponseDto getAnswer(GeminiRequestDto requestDto, Long memberId);
}
