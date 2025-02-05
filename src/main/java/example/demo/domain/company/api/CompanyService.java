package example.demo.domain.company.api;

import example.demo.domain.company.dto.CompanyCodeDto;

public interface CompanyService {
    CompanyCodeDto returnCompanyCode(Long memberId);
}
