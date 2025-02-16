package example.demo.domain.chat.claude.controller;

import example.demo.domain.chat.claude.service.ClaudeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Claude API
 *
 * @Author : 박재성
 * @Since : 02/16/2025
 */

@RestController
@RequestMapping("/api/claude")
@AllArgsConstructor
public class ClaudeController {

    private final ClaudeService claudeService;

    @PostMapping("/chat")
    public ResponseEntity<String> chat(@RequestBody Map<String, String> request) {
        String prompt = request.get("prompt");
        if (prompt == null || prompt.isBlank()) {
            return ResponseEntity.badRequest().body("프롬프트 값이 비어 있습니다.");
        }
        return claudeService.getCompletion(prompt);
    }

}
