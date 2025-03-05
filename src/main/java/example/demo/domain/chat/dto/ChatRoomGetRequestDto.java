package example.demo.domain.chat.dto;

import example.demo.domain.chat.AIModelType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomGetRequestDto {

    private AIModelType aiModelType;
}
