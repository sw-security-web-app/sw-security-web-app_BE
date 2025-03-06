package example.demo.domain.chat.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomRequestDto {

    private Long chatRoomId;
    private LocalDateTime createdAt;
    //최근 대화 미리보기
    private String preview;

    @Builder
    @QueryProjection
    public ChatRoomRequestDto(Long chatRoomId, LocalDateTime createdAt,String preview) {
        this.chatRoomId = chatRoomId;
        this.createdAt = createdAt;
        this.preview=preview;
    }
}
