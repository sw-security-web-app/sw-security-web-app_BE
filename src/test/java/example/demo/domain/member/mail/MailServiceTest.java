package example.demo.domain.member.mail;

import example.demo.domain.member.Member;
import example.demo.domain.member.repository.MemberRepository;
import example.demo.domain.member.dto.request.MemberRequestDto;
import example.demo.util.CreateRandom;
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

    @Autowired
    private MailService mailService; // Inject the mocked mail sender into MailService

    @Autowired
    private MemberRepository memberRepository;


}
