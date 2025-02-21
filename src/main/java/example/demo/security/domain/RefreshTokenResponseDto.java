package example.demo.security.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RefreshTokenResponseDto {
    private String accessToken;
    private String refreshToken;
}
