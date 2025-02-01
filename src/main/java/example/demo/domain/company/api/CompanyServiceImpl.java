package example.demo.domain.company.api;

import example.demo.domain.company.CompanyRepository;
import example.demo.domain.company.dto.CompanyCodeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    //발급받은 코드 리턴
    //TODO:나중에 JWT TOKEN으로 대체합니다.
    public CompanyCodeDto returnCompanyCode(Long memberId){
        return companyRepository.findCompanyCode(memberId);
    }
}
