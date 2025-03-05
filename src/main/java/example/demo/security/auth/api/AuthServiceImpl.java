package example.demo.security.auth.api;

import example.demo.domain.member.Member;
import example.demo.domain.member.MemberErrorCode;
import example.demo.domain.member.MemberStatus;
import example.demo.domain.member.repository.MemberRepository;
import example.demo.error.CommonErrorCode;
import example.demo.error.RestApiException;
import example.demo.security.auth.AuthErrorCode;
import example.demo.security.auth.dto.AccessTokenResponseDto;
import example.demo.security.auth.dto.CustomMemberInfoDto;
import example.demo.security.auth.dto.MemberLoginDto;
import example.demo.security.domain.RefreshToken;
import example.demo.security.exception.SecurityErrorCode;
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
    public AccessTokenResponseDto loginMember(MemberLoginDto loginDto, HttpServletResponse response) {
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
        setRefreshToken(refreshToken, response);

        refresh.putRefreshToken(refreshToken, infoDto.getMemberId());

        //기존 가지고 있는 사용자 refresh Token제거
        return AccessTokenResponseDto.builder()
                .accessToken(accessToken)
                .message("토큰 반환 성공")
                .code(200)
                .build();
    }

    @Override
    public AccessTokenResponseDto refreshAccessToken(String refreshToken, HttpServletResponse response) {
        //refresh Token 유효성 검증
        checkRefreshToken(refreshToken);

        //Redis에 리프레시 토큰 저장유무 확인
        Long memberId=jwtUtil.getMemberId(refreshToken);
        String storedToken=refresh.getRefreshToken(refreshToken);

        if(!refreshToken.equals(storedToken)){
            throw new RestApiException(SecurityErrorCode.INVALID_TOKEN);
        }

        //기존 Refresh Token 삭제
        refresh.removeUserRefreshToken(memberId);

        //새 토큰 발급
        Member member=memberRepository.findById(memberId)
                .orElseThrow(()->new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));
        CustomMemberInfoDto infoDto=CustomMemberInfoDto
                .builder()
                .memberId(memberId)
                .accountLocked(false)
                .memberStatus(member.getMemberStatus())
                .email(member.getEmail())
                .password(member.getPassword())
                .build();

        String newAccessToken=jwtUtil.createAccessToken(infoDto);
        String newRefreshToken=jwtUtil.generateRefreshToken(memberId);

        //새 Refresh 저장
        refresh.putRefreshToken(newRefreshToken,memberId);

        //새로운 Refresh 쿠키 설정
        setRefreshToken(refreshToken, response);

        return AccessTokenResponseDto
                .builder()
                .code(200)
                .accessToken(newAccessToken)
                .message("엑세스 토큰 재발행 성공")
                .build();
    }

    @Override
    public void secessionMember(String token) {
        Long memberId=jwtUtil.getMemberId(token);
        Member findMember=memberRepository.findById(memberId)
                .orElseThrow(()->new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));
        memberRepository.delete(findMember);
    }

    @Override
    public void locking(String token, Long memberId) {
        //Lock 요청을 보낸 유저
        Long requestMemberId=jwtUtil.getMemberId(token);
        Member requestMember=memberRepository.findById(requestMemberId)
                .orElseThrow(()->new RestApiException(AuthErrorCode.NOT_EXIST_MEMBER));

        //Lock이 되는 유저
        Member lockedMember=memberRepository.findById(memberId)
                .orElseThrow(()->new RestApiException(AuthErrorCode.NOT_EXIST_MEMBER));

        validation(requestMember, lockedMember);

        lockedMember.changeMemberLock(true);
        memberRepository.save(lockedMember);
    }

    //예외 처리 메서드
    private static void validation(Member requestMember, Member lockedMember) {
        //같은 회사 인가
        boolean isSameCompany= requestMember.getCompany().getCompanyId().equals(lockedMember.getCompany().getCompanyId());
        if (!isSameCompany){
            throw new RestApiException(AuthErrorCode.NO_AUTHORITIES);
        }

        //Lock당하는 유저가 Manager가 아닌가
        boolean isNotManager= lockedMember.getMemberStatus().equals(MemberStatus.MANAGER);
        if (isNotManager){
            throw new RestApiException(AuthErrorCode.CAN_NOT_LOCKED_MANAGER);
        }
        //Lock 요청을 보낸 유저가 MANAGER인가
        boolean isManagerOfCompany= requestMember.getMemberStatus().equals(MemberStatus.MANAGER);
        if (!isManagerOfCompany){
            throw new RestApiException(AuthErrorCode.NO_AUTHORITIES);
        }
    }

    private static void setRefreshToken(String refreshToken, HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setMaxAge(3*24*60*60);
        cookie.setPath("/");
        response.addCookie(cookie);
    }


    private void checkRefreshToken(String refreshToken){
        if(Boolean.FALSE.equals(jwtUtil.validateToken(refreshToken))){
            throw new RestApiException(SecurityErrorCode.INVALID_TOKEN);

        }
    }
}
