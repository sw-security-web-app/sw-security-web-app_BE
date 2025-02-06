package example.demo.domain.member.api;

import example.demo.domain.member.dto.request.MemberRequestDto;
import example.demo.domain.member.dto.request.SmsCertificationRequestDto;
import example.demo.util.ValidationGroups;
import example.demo.util.ValidationSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    @PostMapping("/api/signup")
    public ResponseEntity<?>signup(@Validated(ValidationSequence.class) @RequestBody MemberRequestDto memberRequestDto){
        memberService.signup(memberRequestDto);
        return ResponseEntity.ok("회원가입 성공");
    }

    @GetMapping("/api/find-email")
    public ResponseEntity<?>findEmail(@RequestParam String phoneNumber){
        memberService.findMemberEmail(phoneNumber);
        return ResponseEntity.ok("휴대폰 번호로 가입한 이메일 정보 전송");
    }

}
