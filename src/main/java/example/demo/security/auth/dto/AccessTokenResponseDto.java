package example.demo.security.auth.dto;

import example.demo.domain.member.MemberStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class AccessTokenResponseDto {
    private String accessToken;
    private String message;
    private Integer code;
    private String userName;
    private MemberStatus role;

}
