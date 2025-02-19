package example.demo.domain.chat.gemini.controller;

import example.demo.domain.chat.gemini.service.QnAService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Gemini API
 *
 * @Author : 박재성
 * @Since : 02/15/2025
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/qna")
public class GeminiController {

    private final QnAService qnAService;

    @PostMapping("/ask")
    public ResponseEntity<Map<String, Object>> askQuestion(@RequestBody Map<String, String> payload) {
        String prompt = payload.get("prompt");
        Map<String, Object> answer = qnAService.getAnswer(prompt);
        return ResponseEntity.ok(answer);
    }
}
