package example.demo.domain.chat.claude.service;

import example.demo.domain.chat.claude.ClaudeErrorCode;
import example.demo.domain.chat.claude.dto.ClaudeRequestDto;
import example.demo.domain.chat.claude.dto.ClaudeResponseDto;
import example.demo.error.CommonErrorCode;
import example.demo.error.RestApiException;
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
public class ClaudeServiceImpl implements ClaudeService {

    @Value("${anthropic.api-key}")
    private String claudeApiKey;

    @Value("${anthropic.url}")
    private String claudeApiUrl;

    @Value("${anthropic.model}")
    private String claudeModel;

    @Value("${anthropic.version}")
    private String claudeVersion;

    private final RestTemplate restTemplate;

    public ClaudeServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ClaudeResponseDto getCompletion(ClaudeRequestDto requestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", claudeApiKey);
        headers.set("anthropic-version", claudeVersion);

        // 요청 JSON 구조
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", claudeModel);
        requestBody.put("max_tokens", 100); // 응답 오는 토큰 수 => 커질 수록 응답 길이 커짐
        requestBody.put("messages", List.of(
                Map.of("role", "user", "content", requestDto.getPrompt())
        ));

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        /*
         Body와 Content에 대해서 일시적 예외처리 완료
        */
        try {
            ResponseEntity<Map> responseEntity = restTemplate.exchange(claudeApiUrl, HttpMethod.POST, requestEntity, Map.class);

            if (responseEntity.getBody() == null) {
                throw new RestApiException(ClaudeErrorCode.NO_EXIST_BODY);
            }
            Object content = extractCompletion(responseEntity.getBody());

            if (content == null) {
                throw new RestApiException(ClaudeErrorCode.NO_EXIST_CONTENT);
            }

            return ClaudeResponseDto.builder()
                    .prompt(content.toString())
                    .build();
        } catch (RestApiException e) {
            throw new RestApiException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // 응답에서 메시지 추출
    private Object extractCompletion(Map<String, Object> responseBody) {
        if (responseBody == null) {
            throw new RestApiException(ClaudeErrorCode.NO_EXIST_BODY);
        }

        Object content = responseBody.get("content");
        if (!(content instanceof List)) {
            throw new RestApiException(ClaudeErrorCode.NO_EXIST_CONTENT);
        }

        List<Map<String, Object>> contentList = (List<Map<String, Object>>) content;
        if (contentList.isEmpty()) {
            throw new RestApiException(ClaudeErrorCode.NO_EXIST_CONTENT);
        }

        Object firstMessage = contentList.get(0).get("text");
        if (firstMessage == null || firstMessage.toString().isBlank()) {
            throw new RestApiException(ClaudeErrorCode.NO_EXIST_CONTENT);
        }

        return firstMessage.toString();
    }
}
