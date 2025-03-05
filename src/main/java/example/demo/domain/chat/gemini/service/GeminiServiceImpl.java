package example.demo.domain.chat.gemini.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import example.demo.domain.chat.AIModelType;
import example.demo.domain.chat.ChatRoom;
import example.demo.domain.chat.ChatRoomErrorCode;
import example.demo.domain.chat.dto.ChatDto;
import example.demo.domain.chat.gemini.GeminiErrorCode;
import example.demo.domain.chat.gemini.dto.GeminiRequestDto;
import example.demo.domain.chat.gemini.dto.GeminiResponseDto;
import example.demo.domain.chat.repository.ChatRoomRepository;
import example.demo.domain.chat.service.ChatService;
import example.demo.error.RestApiException;
import example.demo.verification.util.PythonServerUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

import static org.springframework.web.reactive.function.client.WebClient.*;

/**
 * Gemini API Service Impl
 *
 * @Author : 박재성
 * @Since : 02/15/2025
 */

@Service
public class GeminiServiceImpl implements GeminiService {
    // Access to APIKey and URL [Gemini]
    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final WebClient webClient;
    private final ChatService chatService;
    private final ChatRoomRepository chatRoomRepository;
    private final PythonServerUtil pythonServerUtil;

    public GeminiServiceImpl(Builder webClient, ChatService chatService, ChatRoomRepository chatRoomRepository, PythonServerUtil pythonServerUtil) {
        this.webClient = webClient.build();
        this.chatService = chatService;
        this.chatRoomRepository = chatRoomRepository;
        this.pythonServerUtil = pythonServerUtil;
    }

    @Override
    public GeminiResponseDto getAnswer(GeminiRequestDto requestDto, Long memberId) {
        Long chatRoomId = requestDto.getChatRoomId();

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RestApiException(ChatRoomErrorCode.CHAT_ROOM_NOT_FOUND));

        //* 프롬프트 검열
       // pythonServerUtil.validatePrompt(requestDto.getPrompt());

        // Construct the request payload
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[] {
                        Map.of("parts", new Object[] {
                                Map.of("text", requestDto.getPrompt())
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
            ChatDto chatDto = ChatDto.builder()
                    .modelType(AIModelType.Gemini)
                    .question(requestDto.getPrompt())
                    .answer(text)
                    .chatRoomId(chatRoom.getChatRoomId())
                    .build();
            chatService.saveChat(memberId, chatDto, chatRoomId);

            return new GeminiResponseDto(text);
        } catch (JsonProcessingException e) {
            throw new RestApiException(GeminiErrorCode.GEMINI_JSON_PROCESS_ERROR);
        }
    }
}
