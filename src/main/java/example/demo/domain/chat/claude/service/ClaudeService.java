package example.demo.domain.chat.claude.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Claude API Service
 *
 * @Author : 박재성
 * @Since : 02/16/2025
 */

@Service
public class ClaudeService {

    @Value("${anthropic.api-key}")
    private String apiKey;

    @Value("${anthropic.url}")
    private String apiUrl;

    @Value("${anthropic.model}")
    private String model;

    @Value("${anthropic.version}")
    private String claudeVersion;

    private final RestTemplate restTemplate;

    public ClaudeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> getCompletion(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", apiKey);
        headers.set("anthropic-version", claudeVersion);

        // 요청 JSON 구조
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("max_tokens", 100); // 응답 오는 토큰 수 => 커질 수록 응답 길이 커짐
        requestBody.put("messages", List.of(
                Map.of("role", "user", "content", prompt)
        ));

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, Map.class);

            if (responseEntity.getBody() != null) {
                Object content = extractCompletion(responseEntity.getBody());
                if (content != null) {
                    return ResponseEntity.ok(content.toString());
                }
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("응답 없음");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("오류 발생: " + e.getMessage());
        }
    }

    // 응답에서 메시지 추출
    private Object extractCompletion(Map<String, Object> responseBody) {
        if (responseBody.containsKey("content")) {
            return responseBody.get("content");
        }
        return null;
    }
}
