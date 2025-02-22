package example.demo.domain.chat.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LatestChatRoomResponseDto {

    private final Long chatRoomId;
    private final String latestAnswer;

    @Builder
    @QueryProjection
    public LatestChatRoomResponseDto(Long chatRoomId, String latestAnswer) {
        this.chatRoomId = chatRoomId;
        this.latestAnswer = latestAnswer;
    }
}
