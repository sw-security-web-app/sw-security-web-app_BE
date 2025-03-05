package example.demo.security.auth.api;

import example.demo.domain.member.Member;
import example.demo.domain.member.dto.request.MemberRequestDto;
import example.demo.domain.member.repository.MemberRepository;
import example.demo.security.auth.dto.ChangePasswordRequestDto;
import example.demo.security.util.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@SpringBootTest
@Transactional
class AuthServiceImplTest {
    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @MockitoBean
    private PasswordEncoder encoder;

    @MockitoBean
    private JwtUtil jwtUtil;
    @Test
    @DisplayName("[성공케이스]-회원의 비밀번호를 변경합니다.")
    void changePassword() {
        //given
        MemberRequestDto requestDto=MemberRequestDto.ofGeneral(
                "tkv00@naver.com","김도연","rlaehdus00!!","01012345678","GENERAL"
        );

        Member member=Member.createGeneral(requestDto);
        member=memberRepository.save(member);
        memberRepository.flush();

        String token="token";

        ChangePasswordRequestDto passwordRequestDto= ChangePasswordRequestDto
                .builder()
                .newPassword("rlaehdus00123!!")
                .email(requestDto.getEmail())
                .password(requestDto.getPassword())
                .build();

        when(jwtUtil.getMemberId(token)).thenReturn(member.getMemberId());
        when(encoder.encode(anyString())).thenAnswer(invocation -> "ENCODED_" + invocation.getArgument(0));
        when(encoder.matches(anyString(),anyString())).thenReturn(true);
        // when
        authService.changePassword(token,passwordRequestDto);


        //then
        Member updatedMember=memberRepository.findById(member.getMemberId()).get();
        assertThat(encoder.matches(updatedMember.getPassword(),
                encoder.encode(passwordRequestDto.getNewPassword()))).isTrue();
    }
}