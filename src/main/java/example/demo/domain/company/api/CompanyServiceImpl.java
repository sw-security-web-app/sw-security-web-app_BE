package example.demo.domain.company.api;

import example.demo.domain.company.CompanyErrorCode;
import example.demo.domain.company.dto.response.CompanyResponseDto;
import example.demo.domain.company.repository.CompanyRepository;
import example.demo.domain.company.dto.CompanyCodeDto;
import example.demo.domain.member.Member;
import example.demo.error.RestApiException;
import example.demo.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final JwtUtil jwtUtil;
    //발급받은 코드 리턴
    //TODO:나중에 JWT TOKEN으로 대체합니다.
    public CompanyCodeDto returnCompanyCode(Long memberId){
        return companyRepository.findCompanyCode(memberId);
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyResponseDto getCompanyInfo(String token) {
        CompanyResponseDto companyResponseDto=
                companyRepository.getCompanyInfo(jwtUtil.getMemberId(token));
        if(companyResponseDto==null){
            throw new RestApiException(CompanyErrorCode.NOT_EXIST_COMPANY);
        }
        return companyResponseDto;
    }
}
