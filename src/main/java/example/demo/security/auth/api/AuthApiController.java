package example.demo.security.auth.api;

import example.demo.common.ResponseDto;
import example.demo.domain.member.MemberErrorCode;
import example.demo.error.RestApiException;
import example.demo.security.auth.AuthErrorCode;
import example.demo.security.auth.dto.AccessTokenResponseDto;
import example.demo.security.auth.dto.ChangePasswordRequestDto;
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
@CrossOrigin(origins = "http://172.20.10.2:5173",allowCredentials = "true")
public class AuthApiController {
    private final AuthService authService;

    @PostMapping("login")
    public ResponseEntity<?> login(@Validated(ValidationSequence.class) @RequestBody MemberLoginDto loginDto,
                                   HttpServletResponse response){
        AccessTokenResponseDto accessTokenResponseDto=authService.loginMember(loginDto,response);
        return ResponseEntity.ok(accessTokenResponseDto);
    }

    @PostMapping("refresh")
    public ResponseEntity<?> getRefreshToken(@CookieValue(name = "refreshToken",required = false)String refreshToken,
                                             HttpServletResponse response){
        if(refreshToken==null){
            throw new RestApiException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }
        AccessTokenResponseDto accessTokenResponseDto= authService.refreshAccessToken(refreshToken,response);
        return ResponseEntity.ok(accessTokenResponseDto);
    }

    @PutMapping("change-password")
    public ResponseEntity<?> changePassword(@Validated(ValidationSequence.class) @RequestBody ChangePasswordRequestDto requestDto,
                                            @RequestHeader("Authorization")String token){
        authService.changePassword(token,requestDto);
        return ResponseEntity.ok(ResponseDto.of(true,200,"비밀번호 변경 성공"));
    }
}
