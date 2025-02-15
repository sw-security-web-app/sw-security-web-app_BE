package example.demo.domain.member.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
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

    @QueryProjection
    public MemberInfoResponseDto(String name, String email, String companyName, String companyDept, String companyPosition) {
        this.name = name;
        this.email = email;
        this.companyName = companyName;
        this.companyDept = companyDept;
        this.companyPosition = companyPosition;
    }
}
