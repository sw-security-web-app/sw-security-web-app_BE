package example.demo.domain.member.api;

import example.demo.domain.member.dto.request.MemberRequestDto;
import example.demo.domain.member.dto.request.SmsCertificationRequestDto;
import example.demo.domain.member.sms.SmsCertificationDao;

public interface MemberService {
    void signup(MemberRequestDto memberRequestDto);
    //인증번호 전송
    void sendSms(SmsCertificationRequestDto smsCertificationRequestDto);
    //인증번호 검증
    void verifySms(SmsCertificationRequestDto smsCertificationRequestDto);
}
