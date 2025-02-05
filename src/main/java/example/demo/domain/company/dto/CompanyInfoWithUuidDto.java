package example.demo.domain.company.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class CompanyInfoWithUuidDto {
    private String companyName;
    private String companyDept;

    @QueryProjection
    public CompanyInfoWithUuidDto(String companyName, String companyDept) {
        this.companyName = companyName;
        this.companyDept = companyDept;
    }
}
