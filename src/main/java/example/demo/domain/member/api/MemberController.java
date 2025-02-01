package example.demo.domain.member.api;

import example.demo.domain.member.dto.request.MemberRequestDto;
import example.demo.domain.member.dto.request.SmsCertificationRequestDto;
import example.demo.util.ValidationGroups;
import example.demo.util.ValidationSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    @PostMapping("/api/signup")
    public ResponseEntity<?>signup(@Validated(ValidationSequence.class) @RequestBody MemberRequestDto memberRequestDto){
        memberService.signup(memberRequestDto);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/api/sms-certification/send")
    public ResponseEntity<?> sendSms(@Validated(ValidationSequence.class) @RequestBody SmsCertificationRequestDto requestDto){
        memberService.sendSms(requestDto);
        return ResponseEntity.ok("인증번호 전송");
    }

    @PostMapping("/api/sms-certification/confirm")
    public ResponseEntity<?> confirmSms(@Validated(ValidationSequence.class) @RequestBody SmsCertificationRequestDto requestDto){
        memberService.verifySms(requestDto);
        return ResponseEntity.ok("인증 성공");
    }
}
