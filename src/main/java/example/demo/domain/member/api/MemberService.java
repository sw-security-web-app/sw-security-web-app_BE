package example.demo.domain.member.api;


import example.demo.domain.company.dto.response.CompanyResponseDto;
import example.demo.domain.member.dto.response.CompanyEmployeeResponseDto;
import example.demo.domain.member.dto.response.MemberInfoResponseDto;
import example.demo.security.auth.dto.MemberLoginDto;
import example.demo.domain.member.dto.request.MemberRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
//import example.demo.domain.member.sms.SmsCertificationDao;

public interface MemberService {
    void signup(MemberRequestDto memberRequestDto);

    Page<CompanyEmployeeResponseDto> getAllEmployees(String token, Pageable page);


    MemberInfoResponseDto getMemberInfo(String token);


}
