package example.demo.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatRoomRecentResponseDto {

    private Long chatRoomId;

    private String latestAnswer;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime latestCreatedAt;

    @Builder
    @QueryProjection
    public ChatRoomRecentResponseDto(Long chatRoomId, String latestAnswer, LocalDateTime latestCreatedAt) {
        this.chatRoomId = chatRoomId;
        this.latestAnswer = latestAnswer;
        this.latestCreatedAt = latestCreatedAt;
    }
}
