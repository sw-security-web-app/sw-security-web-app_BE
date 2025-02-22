package example.demo.domain.chat.gpt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import example.demo.domain.chat.gpt.GptErrorCode;
import example.demo.domain.chat.gpt.config.ChatGPTConfig;
import example.demo.domain.chat.gpt.dto.ChatCompletionDto;
import example.demo.error.RestApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
public class ChatGPTServiceImpl implements ChatGPTService {

    private final ChatGPTConfig chatGPTConfig;

    public ChatGPTServiceImpl(ChatGPTConfig chatGPTConfig) {
        this.chatGPTConfig = chatGPTConfig;
    }

    @Value("${openai.url.prompt}")
    private String promptUrl;

    /**
     * 신규 모델에 대한 프롬프트
     */
    @Override
    public Map<String, Object> prompt(ChatCompletionDto chatCompletionDto) {
        log.debug("[+] 신규 프롬프트를 수행합니다.");

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

            return resultMap;
        } catch (JsonProcessingException e) {
            throw new RestApiException(GptErrorCode.GPT_JSON_PROCESS_ERROR);
        } catch (RuntimeException e) {
            throw new RestApiException(GptErrorCode.GPT_API_ERROR);
        }
    }
}
