package example.demo.domain.company.api;

import example.demo.domain.company.dto.CompanyCodeDto;
import example.demo.domain.company.dto.response.CompanyResponseDto;

public interface CompanyService {
    CompanyCodeDto returnCompanyCode(String token);
    CompanyResponseDto getCompanyInfo(String token);
}
