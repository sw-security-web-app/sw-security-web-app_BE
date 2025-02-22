package example.demo.domain.chat.claude.service;

import example.demo.domain.chat.claude.dto.ClaudeRequestDto;
import example.demo.domain.chat.claude.dto.ClaudeResponseDto;
import org.springframework.stereotype.Service;

/**
 * Claude Service Interface
 *
 * @Author : 박재성
 * @Since : 02/16/2025
 */

@Service
public interface ClaudeService {
    ClaudeResponseDto getCompletion(ClaudeRequestDto requestDto);

    /*
    ✔ extractCompletion은 interface와 impl로 구분할 필요 없음!
     */
}
