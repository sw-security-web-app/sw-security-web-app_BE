package example.demo.domain.company.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import example.demo.domain.member.dto.response.CompanyEmployeeResponseDto;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class CompanyResponseDto {
    private String companyName;
    private String companyDept;

    @QueryProjection
    public CompanyResponseDto(String companyName,String companyDept){
        this.companyDept=companyDept;
        this.companyName=companyName;
    }
}
