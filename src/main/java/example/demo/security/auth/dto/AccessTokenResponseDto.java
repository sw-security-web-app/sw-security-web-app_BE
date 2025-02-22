package example.demo.security.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class AccessTokenResponseDto {
    private String accessToken;
    private String message;
    private Integer code;
}
