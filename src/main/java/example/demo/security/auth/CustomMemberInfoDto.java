package example.demo.security.auth;

import example.demo.domain.member.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomMemberInfoDto extends MemberLoginDto {
    private Long memberId;
    private String email;
    private String name;
    private String password;
    private MemberStatus memberStatus;

}
