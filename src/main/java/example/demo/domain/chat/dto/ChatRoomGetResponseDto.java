package example.demo.domain.chat.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatRoomGetResponseDto {

    private Long chatRoomId;

    private LocalDateTime createdAt;

    @Builder
    @QueryProjection
    public ChatRoomGetResponseDto(Long chatRoomId, LocalDateTime createdAt) {
        this.chatRoomId = chatRoomId;
        this.createdAt = createdAt;
    }
}
