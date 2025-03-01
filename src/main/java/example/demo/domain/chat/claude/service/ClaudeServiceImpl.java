package example.demo.domain.chat.claude.service;

import example.demo.domain.chat.AIModelType;
import example.demo.domain.chat.ChatRoom;
import example.demo.domain.chat.ChatRoomErrorCode;
import example.demo.domain.chat.claude.ClaudeErrorCode;
import example.demo.domain.chat.claude.dto.ClaudeRequestDto;
import example.demo.domain.chat.claude.dto.ClaudeResponseDto;
import example.demo.domain.chat.dto.ChatDto;
import example.demo.domain.chat.repository.ChatRoomRepository;
import example.demo.domain.chat.service.ChatService;
import example.demo.error.CommonErrorCode;
import example.demo.error.RestApiException;
import example.demo.verification.util.PythonServerUtil;
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
    private final ChatRoomRepository chatRoomRepository;
    private final ChatService chatService;
    private final PythonServerUtil pythonServerUtil;

    public ClaudeServiceImpl(RestTemplate restTemplate, ChatRoomRepository chatRoomRepository, ChatService chatService, PythonServerUtil pythonServerUtil) {
        this.restTemplate = restTemplate;
        this.chatRoomRepository = chatRoomRepository;
        this.chatService = chatService;
        this.pythonServerUtil = pythonServerUtil;
    }

    @Override
    public ClaudeResponseDto getCompletion(ClaudeRequestDto requestDto, Long memberId) {
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

        Long chatRoomId = requestDto.getChatRoomId();
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RestApiException(ChatRoomErrorCode.CHAT_ROOM_NOT_FOUND));

        //* 프롬프트 검열
        pythonServerUtil.validatePrompt(requestDto.getPrompt());

        try {
            ResponseEntity<Map> responseEntity = restTemplate.exchange(claudeApiUrl, HttpMethod.POST, requestEntity, Map.class);

            if (responseEntity.getBody() == null) {
                throw new RestApiException(ClaudeErrorCode.NO_EXIST_BODY);
            }
            String content = extractCompletion(responseEntity.getBody());

            if (content == null) {
                throw new RestApiException(ClaudeErrorCode.NO_EXIST_CONTENT);
            }

            ChatDto chatDto = ChatDto.builder()
                    .modelType(AIModelType.Claude)
                    .question(requestDto.getPrompt())
                    .answer(content)
                    .chatRoomId(chatRoom.getChatRoomId())
                    .build();
            chatService.saveChat(memberId, chatDto, chatRoomId);

            return new ClaudeResponseDto(content);
        } catch (RestApiException e) {
            throw new RestApiException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // 응답에서 메시지 추출
    private String extractCompletion(Map<String, Object> responseBody) {
        if (responseBody == null) {
            throw new RestApiException(ClaudeErrorCode.NO_EXIST_BODY);
        }

        Object content = responseBody.get("content");
        if (!(content instanceof List)) {
            throw new RestApiException(ClaudeErrorCode.NO_EXIST_CONTENT);
        }

        List<?> contentList = (List<?>) content;
        if (contentList.isEmpty()) {
            throw new RestApiException(ClaudeErrorCode.NO_EXIST_CONTENT);
        }

        if (!(contentList.get(0) instanceof HashMap)) {
            throw new RestApiException(ClaudeErrorCode.NO_EXIST_CONTENT);
        }

        HashMap<String, Object> firstMessage = (HashMap<String, Object>) contentList.get(0);

        // "text" 키가 존재하는지 확인하고 가져오기
        Object text = firstMessage.get("text");
        if (text == null || text.toString().isBlank()) {
            throw new RestApiException(ClaudeErrorCode.NO_EXIST_CONTENT);
        }

        return text.toString();
    }
}
