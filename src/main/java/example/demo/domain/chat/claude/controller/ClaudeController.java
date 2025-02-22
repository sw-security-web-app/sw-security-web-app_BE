package example.demo.domain.chat.claude.controller;

import example.demo.domain.chat.claude.dto.ClaudeRequestDto;
import example.demo.domain.chat.claude.dto.ClaudeResponseDto;
import example.demo.domain.chat.claude.service.ClaudeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/ask")
    public ResponseEntity<ClaudeResponseDto> chat(@Valid @RequestBody ClaudeRequestDto requestDTO) {
        ClaudeResponseDto responseDto = claudeService.getCompletion(requestDTO);
        return ResponseEntity.ok(responseDto);
    }

}
