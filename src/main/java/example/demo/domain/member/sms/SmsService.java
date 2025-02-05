package example.demo.domain.member.sms;

import example.demo.domain.member.dto.request.SmsCertificationRequestDto;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;

public interface SmsService {
    SingleMessageSentResponse sendOne(String to,String verificationCode);
    //인증번호 전송
    void sendSms(SmsCertificationRequestDto smsCertificationRequestDto);
}
