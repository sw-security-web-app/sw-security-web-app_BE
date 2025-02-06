package example.demo.security.auth;

import example.demo.domain.member.Member;
import example.demo.domain.member.repository.MemberRepository;
import example.demo.error.RestApiException;
import example.demo.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
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

        CustomMemberInfoDto infoDto=CustomMemberInfoDto.builder()
                .email(findMember.getEmail())
                .password(findMember.getPassword())
                .memberStatus(findMember.getMemberStatus())
                .memberId(findMember.getMemberId())
                .build();
        return jwtUtil.createAccessToken(infoDto);
    }
}
