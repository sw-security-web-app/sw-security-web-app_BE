package example.demo.domain.member.sms;

import example.demo.domain.member.dto.request.MemberFindEmailRequestDto;
import example.demo.domain.member.dto.request.SmsCertificationRequestDto;
import example.demo.util.ValidationSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SmsController {
    private final SmsService smsService;
    private final SmsVerificationService smsVerificationService;

    @PostMapping("/api/sms-certification/send")
    public ResponseEntity<?> sendSms(@Validated(ValidationSequence.class) @RequestBody SmsCertificationRequestDto requestDto){
        smsService.sendSms(requestDto);
        return ResponseEntity.ok("인증번호 전송");
    }

    @PostMapping("/api/sms-certification/confirm")
    public ResponseEntity<?> confirmSms(@Validated(ValidationSequence.class) @RequestBody SmsCertificationRequestDto requestDto){
        smsVerificationService.verifySms(requestDto);
        return ResponseEntity.ok("인증 성공");
    }

    @PostMapping ("/api/find-email")
    public ResponseEntity<?> findEmail(@Validated(ValidationSequence.class) @RequestBody MemberFindEmailRequestDto requestDto){
        smsService.sendMemberEmail(requestDto);
        return ResponseEntity.ok("전송 성공");
    }
}
