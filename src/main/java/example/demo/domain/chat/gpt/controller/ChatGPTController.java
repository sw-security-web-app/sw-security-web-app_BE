package example.demo.domain.chat.gpt.controller;

import example.demo.domain.chat.gpt.dto.ChatCompletionDto;
import example.demo.domain.chat.gpt.service.ChatGPTService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * ChatGPT API
 *
 * @Author : 박재성
 * @Since : 02/15/2025
 */

@Slf4j
@RestController
@RequestMapping(value = "/api/chat-gpt")
public class ChatGPTController {

    private final ChatGPTService chatGPTService;

    public ChatGPTController(ChatGPTService chatGPTService) {
        this.chatGPTService = chatGPTService;
    }

    /**
     * @POST [API] 최신 ChatGPT 프롬프트 명령어 수행
     */
    @PostMapping("/ask")
    public ResponseEntity<Map<String, Object>> selectPrompt(@RequestBody ChatCompletionDto chatCompletionDto) {
        log.debug("param :: " + chatCompletionDto.toString());
        Map<String, Object> result = chatGPTService.prompt(chatCompletionDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
