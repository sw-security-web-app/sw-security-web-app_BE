package example.demo.domain.chat.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChatTotalDetailResponseDto {
    private Long lastChatId;
    private List<ChatDetailResponseDto> array;

}
