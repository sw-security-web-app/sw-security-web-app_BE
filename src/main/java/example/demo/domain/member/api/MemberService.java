package example.demo.domain.member.api;

import example.demo.domain.company.dto.response.CompanyResponseDto;
import example.demo.security.auth.dto.MemberLoginDto;
import example.demo.domain.member.dto.request.MemberRequestDto;
import org.springframework.data.domain.Pageable;
//import example.demo.domain.member.sms.SmsCertificationDao;

public interface MemberService {
    void signup(MemberRequestDto memberRequestDto);
    CompanyResponseDto getAllEmployees(String token, Pageable page);
}
