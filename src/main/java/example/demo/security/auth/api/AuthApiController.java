package example.demo.security.auth.api;

import example.demo.security.auth.dto.MemberLoginDto;
import example.demo.util.ValidationSequence;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthApiController {
    private final AuthService authService;

    @PostMapping("login")
    public ResponseEntity<?> login(@Validated(ValidationSequence.class) @RequestBody MemberLoginDto loginDto, HttpServletResponse response){
        String token=authService.loginMember(loginDto,response);
        return ResponseEntity.ok().body(token);
    }
}
