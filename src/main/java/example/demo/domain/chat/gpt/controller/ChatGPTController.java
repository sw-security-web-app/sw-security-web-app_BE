package example.demo.domain.chat.gpt.controller;

import example.demo.domain.chat.gpt.dto.ChatGPTRequestDto;
import example.demo.domain.chat.gpt.dto.ChatGPTResponseDto;
import example.demo.domain.chat.gpt.service.ChatGPTService;
import example.demo.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ChatGPT API
 *
 * @Author : 박재성
 * @Since : 02/15/2025
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/chat-gpt")
public class ChatGPTController {

    private final ChatGPTService chatGPTService;
    private final JwtUtil jwtUtil;

    /**
     * @POST [API] 최신 ChatGPT 프롬프트 명령어 수행
     */
    @PostMapping("/ask")
    public ResponseEntity<?> selectPrompt(@RequestBody ChatGPTRequestDto requestDto,
                                          @RequestHeader("Authorization") String token,
                                          @RequestHeader("X-ChatRoom-Id") Long chatRoomId) {
        Long memberId = jwtUtil.getMemberId(token);
        requestDto.setChatRoomId(chatRoomId);
        ChatGPTResponseDto responseDto = chatGPTService.prompt(requestDto, memberId);
        return ResponseEntity.ok(responseDto);
    }
}
