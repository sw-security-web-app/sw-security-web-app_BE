package example.demo.domain.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatRoomGetResponseDto {

    private Long chatRoomId;

    private String previewQuestion;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    @Builder
    @QueryProjection
    public ChatRoomGetResponseDto(Long chatRoomId, String previewQuestion, LocalDateTime createdAt) {
        this.chatRoomId = chatRoomId;
        this.previewQuestion = previewQuestion;
        this.createdAt = createdAt;
    }
}
