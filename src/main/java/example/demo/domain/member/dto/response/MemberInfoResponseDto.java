package example.demo.domain.member.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import example.demo.domain.member.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberInfoResponseDto {
    //이름
    private String name;
    //이메일
    private String email;
    //회사명
    private String companyName;
    //부서명
    private String companyDept;
    //직책
    private String companyPosition;
    //유저 종류
    private MemberStatus memberStatus;

    @QueryProjection
    @Builder
    public MemberInfoResponseDto(String name, String email, String companyName, String companyDept, String companyPosition,MemberStatus memberStatus) {
        this.name = name;
        this.email = email;
        this.companyName = companyName;
        this.companyDept = companyDept;
        this.companyPosition = companyPosition;
        this.memberStatus=memberStatus;
    }
}
