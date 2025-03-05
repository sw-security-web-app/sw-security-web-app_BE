package example.demo.domain.chat.controller;

import example.demo.domain.chat.AIModelType;
import example.demo.domain.chat.dto.ChatRoomGetRequestDto;
import example.demo.domain.chat.dto.ChatRoomGetResponseDto;
import example.demo.domain.chat.dto.ChatRoomRecentResponseDto;
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
    public ResponseEntity<?> getChatRoom(@RequestHeader("Authorization") String token,
                                         @RequestParam("aiModelType") AIModelType aiModelType) {
        Long memberId = jwtUtil.getMemberId(token);
        List<ChatRoomGetResponseDto> chatRoomList = chatRoomService.getChatRoomList(memberId, aiModelType);
        return ResponseEntity.ok(chatRoomList);
    }

    @GetMapping("/latest")
    public ResponseEntity<?> getLatestChatRoom(@RequestHeader("Authorization") String token,
                                               @RequestParam("aiModelType") AIModelType aiModelType) {
        Long memberId = jwtUtil.getMemberId(token);
        List<ChatRoomRecentResponseDto> latestChatRoom = chatRoomService.getLatestChatRoom(memberId, aiModelType);
        return ResponseEntity.ok(latestChatRoom);
    }
}
