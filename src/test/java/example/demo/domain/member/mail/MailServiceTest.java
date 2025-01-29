package example.demo.domain.member.mail;

import jakarta.mail.internet.MimeMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class MailServiceTest {
    @Mock
    private JavaMailSender javaMailSender; // Mock the mail sender

    @Autowired
    @InjectMocks
    private MailService mailService; // Inject the mocked mail sender into MailService

    @DisplayName("이메일 인증 번호는 100000과 999999 사이의 랜덤한 값이 생성됩니다.")
    @Test
    void createValidationNumber(){
        //given//when
        int number= mailService.createValidationNumber();
        //then
        assertThat(number).isBetween(100000,999999);
    }
}
