package example.demo.domain.chat.gemini.controller;

import example.demo.domain.chat.gemini.dto.GeminiRequestDto;
import example.demo.domain.chat.gemini.dto.GeminiResponseDto;
import example.demo.domain.chat.gemini.service.GeminiService;
import example.demo.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final JwtUtil jwtUtil;

    @PostMapping("/ask")
    public ResponseEntity<?> askQuestion(@RequestBody GeminiRequestDto requestDto,
                                         @RequestHeader("Authorization") String token) {
        Long memberId = jwtUtil.getMemberId(token);
        GeminiResponseDto responseDto = geminiService.getAnswer(requestDto, memberId);
        return ResponseEntity.ok(responseDto);
    }
}
