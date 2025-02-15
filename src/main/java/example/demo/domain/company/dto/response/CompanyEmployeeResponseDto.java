package example.demo.domain.company.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyEmployeeResponseDto {
    private String companyPosition;
    private String name;
    private String email;

    @QueryProjection
    @Builder
    public CompanyEmployeeResponseDto(String companyPosition, String name, String email) {
        this.companyPosition = companyPosition;
        this.name = name;
        this.email = email;
    }
}
