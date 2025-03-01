package example.demo.security.auth;

import example.demo.domain.member.Member;
import example.demo.domain.member.repository.MemberRepository;
import example.demo.error.RestApiException;
import example.demo.security.auth.dto.CustomMemberInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomMemberDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public CustomMemberDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member=memberRepository.findByEmail(email)
                .orElseThrow(()->new RestApiException(AuthErrorCode.NOT_EXIST_MEMBER));
        CustomMemberInfoDto dto=CustomMemberInfoDto.builder()
                .email(member.getEmail())
                .accountLocked(member.isAccountLocked())
                .memberStatus(member.getMemberStatus())
                .password(member.getPassword())
                .build();
        return new CustomMemberDetails(dto);
    }
}
