package example.demo.domain.chat.claude.controller;

import example.demo.domain.chat.claude.dto.ClaudeRequestDto;
import example.demo.domain.chat.claude.dto.ClaudeResponseDto;
import example.demo.domain.chat.claude.service.ClaudeService;
import example.demo.security.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Claude API
 *
 * @Author : 박재성
 * @Since : 02/16/2025
 */

@RestController
@RequestMapping("/api/claude")
@RequiredArgsConstructor
public class ClaudeController {

    private final ClaudeService claudeService;
    private final JwtUtil jwtUtil;

    @PostMapping("/ask")
    public ResponseEntity<?> chat(@Valid @RequestBody ClaudeRequestDto requestDTO,
                                  @RequestHeader("Authorization") String token) {
        Long memberId = jwtUtil.getMemberId(token);
        ClaudeResponseDto responseDto = claudeService.getCompletion(requestDTO, memberId);
        return ResponseEntity.ok(responseDto);
    }

}
