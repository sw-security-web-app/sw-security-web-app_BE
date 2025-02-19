package example.demo.domain.chat.gemini.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import example.demo.domain.chat.gemini.GeminiErrorCode;
import example.demo.error.RestApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

/**
 * Gemini API Service Impl
 *
 * @Author : 박재성
 * @Since : 02/15/2025
 */

@Service
public class QnAServiceImpl implements QnAService {
    // Access to APIKey and URL [Gemini]
    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final WebClient webClient;

    public QnAServiceImpl(WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }

    @Override
    public Map<String, Object> getAnswer(String question) {
        // Construct the request payload
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[] {
                        Map.of("parts", new Object[] {
                                Map.of("text", question)
                        })
                }
        );

        // Make API Call
        String response = webClient.post()
                    .uri(geminiApiUrl + geminiApiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseBody = objectMapper.readValue(response, new TypeReference<>() {});

            // "candidates" 리스트에서 첫 번째 객체 가져오기
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");
            if (candidates == null || candidates.isEmpty()) {
                throw new RestApiException(GeminiErrorCode.GEMINI_NO_CANDIDATES);
            }

            // "content" 내부의 "parts"에서 "text" 값 가져오기
            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");

            if (parts == null || parts.isEmpty()) {
                throw new RestApiException(GeminiErrorCode.GEMINI_NO_TEXT_PART);
            }

            // "text" 값 가져와서 "prompt" 키로 응답
            String text = parts.get(0).get("text").toString();

            return Map.of("prompt", text);
        } catch (JsonProcessingException e) {
            throw new RestApiException(GeminiErrorCode.GEMINI_JSON_PROCESS_ERROR);
        }
    }
}
