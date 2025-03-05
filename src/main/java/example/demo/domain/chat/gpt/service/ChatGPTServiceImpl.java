package example.demo.domain.chat.gpt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import example.demo.domain.chat.AIModelType;
import example.demo.domain.chat.ChatRoom;
import example.demo.domain.chat.ChatRoomErrorCode;
import example.demo.domain.chat.dto.ChatDto;
import example.demo.domain.chat.gpt.GptErrorCode;
import example.demo.domain.chat.gpt.config.ChatGPTConfig;
import example.demo.domain.chat.gpt.dto.ChatCompletionDto;
import example.demo.domain.chat.gpt.dto.ChatGPTRequestDto;
import example.demo.domain.chat.gpt.dto.ChatGPTResponseDto;
import example.demo.domain.chat.gpt.dto.ChatRequestMsgDto;
import example.demo.domain.chat.repository.ChatRoomRepository;
import example.demo.domain.chat.service.ChatService;
import example.demo.error.RestApiException;
import example.demo.util.PythonServerUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ChatGPT Service 구현부
 *
 * @Author : 박재성
 * @Since : 02/15/2025
 *
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatGPTServiceImpl implements ChatGPTService {

    private final ChatGPTConfig chatGPTConfig;
    private final ChatService chatService;
    private final ChatRoomRepository chatRoomRepository;
    private final PythonServerUtil pythonServerUtil;

    @Value("${openai.url.prompt}")
    private String promptUrl;

    /**
     * 신규 모델에 대한 프롬프트
     */
    @Override
    public ChatGPTResponseDto prompt(ChatGPTRequestDto requestDto, Long memberId) {
        Long chatRoomId = requestDto.getChatRoomId();
        String prompt = requestDto.getPrompt();
        String model = requestDto.getModel();

        if (chatRoomId == null || prompt == null || model == null) {
            throw new RestApiException(GptErrorCode.GPT_INVALID_REQUEST);
        }

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RestApiException(ChatRoomErrorCode.CHAT_ROOM_NOT_FOUND));

        //* 프롬프트 검열
       // pythonServerUtil.validatePrompt(prompt);

        List<ChatRequestMsgDto> messages = new ArrayList<>();
        messages.add(ChatRequestMsgDto.builder()
                .role("user")
                .content(prompt)
                .build());
        ChatCompletionDto chatCompletionDto = ChatCompletionDto.builder()
                .model(model)
                .messages(messages)
                .build();

        // 토큰 정보가 포함된 Header를 가져옵니다.
        HttpHeaders headers = chatGPTConfig.httpHeaders();

        // 통신을 위한 RestTemplate을 구성합니다.
        HttpEntity<ChatCompletionDto> requestEntity = new HttpEntity<>(chatCompletionDto, headers);
        ResponseEntity<String> response = chatGPTConfig
                .restTemplate()
                .exchange(promptUrl, HttpMethod.POST, requestEntity, String.class);
        try {
            // String -> HashMap 역직렬화를 구성합니다.
            ObjectMapper om = new ObjectMapper();
            Map<String, Object> responseBody = om.readValue(response.getBody(), new TypeReference<>() {});
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");

            if (choices == null || choices.isEmpty()) {
                throw new RestApiException(GptErrorCode.GPT_NO_RESPONSE);
            }

            Map<String, Object> firstChoice = choices.get(0);
            Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");

            if (message == null || !message.containsKey("content")) {
                throw new RestApiException(GptErrorCode.GPT_NO_CONTENT);
            }

            // "content" 값만 추출하여 반환
            String content = message.get("content").toString();
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("content", content);

            String question = requestDto.getPrompt();

            ChatDto chatDto = ChatDto.builder()
                    .modelType(AIModelType.ChatGPT)
                    .question(question)
                    .answer(content)
                    .chatRoomId(chatRoom.getChatRoomId())
                    .build();

            chatService.saveChat(memberId, chatDto, chatRoomId);
            return new ChatGPTResponseDto(content);
        } catch (JsonProcessingException e) {
            throw new RestApiException(GptErrorCode.GPT_JSON_PROCESS_ERROR);
        } catch (RuntimeException e) {
            throw new RestApiException(GptErrorCode.GPT_API_ERROR);
        }
    }
}
