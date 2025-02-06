package example.demo.security.auth;

import example.demo.domain.member.Member;
import example.demo.domain.member.repository.MemberRepository;
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
    public String addMember(MemberLoginDto loginDto) {
        String email=loginDto.getEmail();
        String password=loginDto.getPassword();
        //Member findMember=memberRepository.findByEmailAndPhoneNumber(email);
        return null;
    }
}
