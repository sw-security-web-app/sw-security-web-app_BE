package example.demo.domain.company.api;

import example.demo.domain.company.dto.CompanyCodeDto;
import example.demo.domain.company.dto.response.CompanyResponseDto;

public interface CompanyService {
    CompanyCodeDto returnCompanyCode(Long companyId);
    CompanyResponseDto getCompanyInfo(String token);
}
