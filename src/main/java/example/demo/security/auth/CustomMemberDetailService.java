package example.demo.security.auth;

import example.demo.domain.member.Member;
import example.demo.domain.member.repository.MemberRepository;
import example.demo.error.RestApiException;
import example.demo.security.auth.dto.CustomMemberInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.LockedException;
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
        //유저의 잠금 상태 확인
        if(member.isAccountLocked()){
            throw new LockedException("계정이 잠겨있습니다. 관리자에게 문의해주세요.");
        }
        CustomMemberInfoDto dto=CustomMemberInfoDto.builder()
                .email(member.getEmail())
                .accountLocked(member.isAccountLocked())
                .memberStatus(member.getMemberStatus())
                .password(member.getPassword())
                .build();
        return new CustomMemberDetails(dto);
    }
}
