package example.demo.domain.chat.controller;

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

    @GetMapping("/latest")
    public ResponseEntity<?> getLatestChatRoom(@RequestHeader("Authorization") String token) {
        Long memberId = jwtUtil.getMemberId(token);
        List<ChatRoomRecentResponseDto> latestChatRoom = chatRoomService.getLatestChatRoom(memberId);
        return ResponseEntity.ok(latestChatRoom);
    }






    //무한 스크롤

    /**
     *
     * @param token 토큰
     * @param id 마지막 채팅 Id
     * @param chatRoomId 채팅방 Id
     * @return No offset으로 구현한 최근 채팅 순으로(idex)활용으로 10개씩 대화 내용 리스트
     */
    @GetMapping("/{chatRoomId}")
    public ResponseEntity<?> getChatList(@RequestHeader("Authorization")String token,
                                         @RequestParam(required = false)Long id,
                                         @PathVariable("chatRoomId")Long chatRoomId){

    }
}
