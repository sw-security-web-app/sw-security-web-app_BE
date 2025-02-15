package example.demo.domain.company.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import example.demo.domain.member.dto.response.CompanyEmployeeResponseDto;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyResponseDto {
    private String companyName;
    private String companyDept;
    private int companyTotal;
    private List<CompanyEmployeeResponseDto> companyEmployees;
    private int totalPage;

    @QueryProjection
    @Builder
    public CompanyResponseDto(String companyName, String companyDept, int companyTotal, List<CompanyEmployeeResponseDto> companyEmployees, int totalPage) {
        this.companyName = companyName;
        this.companyDept = companyDept;
        this.companyTotal = companyTotal;
        this.companyEmployees = companyEmployees;
        this.totalPage = totalPage;
    }
}
