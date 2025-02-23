package example.demo.domain.chat.gpt.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatGPTRequestDto {
    private String model;

    private String prompt;

    @Setter
    private Long chatRoomId;
}
