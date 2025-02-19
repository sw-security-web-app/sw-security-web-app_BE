package example.demo.domain.chat.claude.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ClaudeRequestDto {

    @NotBlank(message = "프롬프트 값이 비어있습니다.")
    private String prompt;
}
