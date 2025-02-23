package example.demo.domain.member.sms;

import example.demo.domain.member.dto.request.MemberFindEmailRequestDto;
import example.demo.domain.member.dto.request.SmsCertificationRequestDto;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;

public interface SmsService {

    //인증번호 전송
    void sendSms(SmsCertificationRequestDto smsCertificationRequestDto);
    //휴대폰 번호로 가입한 이메일 정보 전송
    void sendMemberEmail(MemberFindEmailRequestDto requestDto);
}
