package example.demo.domain.member.mail;

import example.demo.domain.member.Member;
import example.demo.domain.member.repository.MemberRepository;
import example.demo.domain.member.dto.request.MemberRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class MailServiceTest {
    @Mock
    private JavaMailSender javaMailSender; // Mock the mail sender

    @Autowired
    @InjectMocks
    private MailService mailService; // Inject the mocked mail sender into MailService

    @Autowired
    private MemberRepository memberRepository;
    @DisplayName("이메일 인증 번호는 100000과 999999 사이의 랜덤한 값이 생성됩니다.")
    @Test
    void createValidationNumber(){
        //given//when
        int number= mailService.createValidationNumber();
        //then
        assertThat(number).isBetween(100000,999999);
    }

    @Test
    @DisplayName("이메일 중복검사를 실시합니다.")
    void isDuplicatedEmail(){
        //given
        Member member1=Member.createGeneral(
                MemberRequestDto.ofGeneral("tkv00@naver.com","김도연","rlaehdsus!!2","01050299999","GENERAL")
        );
        memberRepository.save(member1);

        //when
        boolean isDuplicated1=mailService.isDuplicatedEmail(member1.getEmail());
        boolean isDuplicated2=mailService.isDuplicatedEmail("tkv@naver.com");

        //then
        assertThat(isDuplicated1).isTrue();
        assertThat(isDuplicated2).isFalse();
    }
}
