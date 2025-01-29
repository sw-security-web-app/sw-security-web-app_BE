package example.demo.domain.company.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class CompanyCodeDto {
    private String companyCode;

    @QueryProjection
    public CompanyCodeDto(String companyCode) {
        this.companyCode = companyCode;
    }
}
