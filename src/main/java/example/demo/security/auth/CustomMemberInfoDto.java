package example.demo.security.auth;

import example.demo.domain.member.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomMemberInfoDto extends MemberLoginDto {
    private Long memberId;
    private String email;
    private String password;
    private MemberStatus memberStatus;

}
