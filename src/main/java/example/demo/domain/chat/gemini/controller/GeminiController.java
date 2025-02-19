package example.demo.domain.chat.gemini.controller;

import example.demo.domain.chat.gemini.dto.GeminiRequestDto;
import example.demo.domain.chat.gemini.dto.GeminiResponseDto;
import example.demo.domain.chat.gemini.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Gemini API
 *
 * @Author : 박재성
 * @Since : 02/15/2025
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gemini")
public class GeminiController {

    private final GeminiService geminiService;

    @PostMapping("/ask")
    public ResponseEntity<GeminiResponseDto> askQuestion(@RequestBody GeminiRequestDto requestDto) {
        GeminiResponseDto responseDto = geminiService.getAnswer(requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
