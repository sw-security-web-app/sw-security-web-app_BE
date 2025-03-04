package example.demo.domain.chat.controller;

import example.demo.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;
    //무한 스크롤
    /**
     *
     * @param token 토큰
     * @param id 마지막 채팅 Id
     * @param chatRoomId 채팅방 Id
     * @return No offset으로 구현한 최근 채팅 순으로(idex)활용으로 10개씩 대화 내용 리스트
     */
    @GetMapping("/detail/{chatRoomId}")
    public ResponseEntity<?> getChatList(@RequestHeader("Authorization")String token,
                                         @RequestParam(required = false,defaultValue = "1")Long id,
                                         @RequestParam(required = false,defaultValue = "10")int size,
                                         @PathVariable("chatRoomId")Long chatRoomId){
        return ResponseEntity.ok(chatService.getDetailChattingContent(chatRoomId,token,id,size));
    }
}
