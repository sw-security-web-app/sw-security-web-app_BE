package example.demo.security.auth.api;

import example.demo.security.auth.dto.AccessTokenResponseDto;
import example.demo.security.auth.dto.MemberLoginDto;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    AccessTokenResponseDto loginMember(MemberLoginDto loginDto, HttpServletResponse response);
    AccessTokenResponseDto refreshAccessToken(String refreshToken,HttpServletResponse response);
    void secessionMember(String token);
    void locking(String token, Long memberId,String type);
}
