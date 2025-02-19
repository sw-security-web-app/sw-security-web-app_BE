package example.demo.domain.chat.claude.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClaudeResponseDto {
    private String prompt;
}
