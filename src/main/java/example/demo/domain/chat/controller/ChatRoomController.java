package example.demo.domain.chat.controller;

import example.demo.domain.chat.dto.ChatDto;
import example.demo.domain.chat.dto.ChatRoomRequestDto;
import example.demo.domain.chat.dto.ChatRoomResponseDto;
import example.demo.domain.chat.service.ChatRoomService;
import example.demo.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat-room")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final JwtUtil jwtUtil;

    @PostMapping("/create")
    public ResponseEntity<?> createChatRoom(@RequestHeader("Authorization") String token) {
        Long memberId = jwtUtil.getMemberId(token);
        ChatRoomResponseDto chatRoom = chatRoomService.createChatRoom(memberId);
        return ResponseEntity.ok(chatRoom);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getChatList(@RequestBody ChatRoomRequestDto requestDto,
                                         @RequestHeader("Authorization") String token) {
        Long memberId = jwtUtil.getMemberId(token);
        List<ChatDto> messages = chatRoomService.getChatListByChatRoomId(requestDto.getChatRoomId());
        return ResponseEntity.ok(messages);
    }
}
