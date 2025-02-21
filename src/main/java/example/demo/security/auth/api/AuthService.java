package example.demo.security.auth.api;

import example.demo.security.auth.dto.MemberLoginDto;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    String loginMember(MemberLoginDto loginDto, HttpServletResponse response);
    String refreshAccessToken(String refreshToken,HttpServletResponse response);
}
