package example.demo.domain.chat.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatRoomResponseDto {

    private final Long chatRoomId;

    @Builder
    @QueryProjection
    public ChatRoomResponseDto(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }
}
