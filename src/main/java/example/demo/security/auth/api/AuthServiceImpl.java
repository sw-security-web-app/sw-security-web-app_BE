package example.demo.security.auth.api;

import example.demo.domain.member.Member;
import example.demo.domain.member.repository.MemberRepository;
import example.demo.error.RestApiException;
import example.demo.security.auth.AuthErrorCode;
import example.demo.security.auth.dto.CustomMemberInfoDto;
import example.demo.security.auth.dto.MemberLoginDto;
import example.demo.security.domain.RefreshToken;
import example.demo.security.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    private final RefreshToken refresh;

    @Override
    public String loginMember(MemberLoginDto loginDto, HttpServletResponse response) {
        String email = loginDto.getEmail();
        String password = loginDto.getPassword();
        Member findMember = memberRepository.findByEmail(email).orElseThrow(
                () -> new RestApiException(AuthErrorCode.INVALID_EMAIL_OR_PASSWORD)
        );

        if (!encoder.matches(password, findMember.getPassword())) {
            throw new RestApiException(AuthErrorCode.INVALID_EMAIL_OR_PASSWORD);
        }

        CustomMemberInfoDto infoDto = new CustomMemberInfoDto(
                findMember.getMemberId(), email, password, findMember.getMemberStatus(), findMember.isAccountLocked()
        );
        //기존 refresh 삭제
        refresh.removeUserRefreshToken(infoDto.getMemberId());

        //Jwt 토큰 생성
        String accessToken = jwtUtil.createAccessToken(infoDto);
        //Refresh token 생성
        String refreshToken = jwtUtil.generateRefreshToken(infoDto.getMemberId());

        //HTTP-ONL 쿠키 설정
        Cookie cookie = new Cookie("refreshToken",refreshToken);
        cookie.setMaxAge(3*24*60*60);
        cookie.setPath("/");
        response.addCookie(cookie);

        refresh.putRefreshToken(refreshToken, infoDto.getMemberId());

        //기존 가지고 있는 사용자 refresh Token제거
        return accessToken;
    }
}
