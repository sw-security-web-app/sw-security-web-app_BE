package example.demo.domain.member.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyEmployeeResponseDto {
    private String companyPosition;
    private String name;
    private String email;
    private Long memberId;

    @QueryProjection
    @Builder
    public CompanyEmployeeResponseDto(String companyPosition, String name, String email,Long memberId) {
        this.companyPosition = companyPosition;
        this.name = name;
        this.email = email;
        this.memberId=memberId;
    }
}
