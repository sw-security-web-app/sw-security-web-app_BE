package example.demo.domain.member.sms;

import example.demo.domain.member.dto.request.SmsCertificationRequestDto;

public interface SmsVerificationService {
    void verifySms(SmsCertificationRequestDto smsCertificationRequestDto);
}
