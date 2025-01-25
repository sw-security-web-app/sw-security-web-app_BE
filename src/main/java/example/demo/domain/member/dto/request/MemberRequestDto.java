package example.demo.domain.member.dto.request;

import example.demo.domain.member.MemberStatus;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class MemberRequestDto {
    private String email;
    private String name;
    private String password;
    private MemberStatus memberStatus;
    private String companyName;
    private String companyDept;
    private String companyPosition;
}
