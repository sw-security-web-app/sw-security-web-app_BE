package example.demo.domain.chat.gpt.dto;

import lombok.*;

/**
 *
 * @Author : 박재성
 * @Since : 02/15/2025
 *
 */

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRequestMsgDto {

    private String role;

    private String content;

    @Builder
    public ChatRequestMsgDto(String role, String content) {
        this.role = role;
        this.content = content;
    }
}
