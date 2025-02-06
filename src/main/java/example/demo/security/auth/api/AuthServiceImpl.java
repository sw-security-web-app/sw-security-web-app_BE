package example.demo.security.auth.api;

import example.demo.domain.member.Member;
import example.demo.domain.member.repository.MemberRepository;
import example.demo.error.RestApiException;
import example.demo.security.auth.AuthErrorCode;
import example.demo.security.auth.dto.CustomMemberInfoDto;
import example.demo.security.auth.dto.MemberLoginDto;
import example.demo.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;

    @Override
    public String loginMember(MemberLoginDto loginDto) {
        String email=loginDto.getEmail();
        String password=loginDto.getPassword();
        Member findMember=memberRepository.findByEmail(email).orElseThrow(
                ()-> new RestApiException(AuthErrorCode.NOT_EXIST_EMAIL)
        );

        if (!encoder.matches(password,findMember.getPassword())){
            throw new RestApiException(AuthErrorCode.INVALID_EMAIL_OR_PASSWORD);
        }

        CustomMemberInfoDto infoDto=new CustomMemberInfoDto(
                findMember.getMemberId(),email,password,findMember.getMemberStatus()
        );
        return jwtUtil.createAccessToken(infoDto);
    }
}
