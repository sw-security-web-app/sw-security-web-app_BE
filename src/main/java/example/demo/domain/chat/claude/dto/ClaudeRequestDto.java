package example.demo.domain.chat.claude.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClaudeRequestDto {

    @NotNull(message = "채팅방 값이 비어있습니다.")
    private Long chatRoomId;

    @NotBlank(message = "프롬프트 값이 비어있습니다.")
    private String prompt;
}
