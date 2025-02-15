package example.demo.domain.member.api;

import example.demo.domain.member.dto.response.MemberInfoResponseDto;
import example.demo.security.auth.dto.MemberLoginDto;
import example.demo.domain.member.dto.request.MemberRequestDto;
//import example.demo.domain.member.sms.SmsCertificationDao;

public interface MemberService {
    void signup(MemberRequestDto memberRequestDto);

    MemberInfoResponseDto getMemberInfo(String token);

}
