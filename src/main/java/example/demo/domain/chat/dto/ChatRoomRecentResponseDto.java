package example.demo.domain.chat.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatRoomRecentResponseDto {

    private Long chatRoomId;

    private String latestAnswer;

    @Builder
    @QueryProjection
    public ChatRoomRecentResponseDto(Long chatRoomId, String latestAnswer) {
        this.chatRoomId = chatRoomId;
        this.latestAnswer = latestAnswer;
    }
}
