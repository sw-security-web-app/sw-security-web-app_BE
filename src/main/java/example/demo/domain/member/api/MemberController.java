package example.demo.domain.member.api;

import example.demo.domain.member.dto.request.MemberRequestDto;
import example.demo.domain.member.dto.request.SmsCertificationRequestDto;
import example.demo.domain.member.dto.response.CompanyEmployeeResponseDto;
import example.demo.util.ValidationGroups;
import example.demo.util.ValidationSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
    @GetMapping("/api/employee-list")
    public ResponseEntity<?>getEmployees(@RequestHeader("Authorization")String token, @PageableDefault(sort = "name") Pageable pageable){
        Page<CompanyEmployeeResponseDto> employees=memberService.getAllEmployees(token,pageable);
        return ResponseEntity.ok(employees);
    }


}
