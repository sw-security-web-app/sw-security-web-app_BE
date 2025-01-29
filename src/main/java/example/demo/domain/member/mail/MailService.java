package example.demo.domain.member.mail;

import example.demo.domain.member.MemberErrorCode;
import example.demo.error.RestApiException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    static final String senderEmail="tkv0098@gmail.com";
    private final ConcurrentHashMap<String,VerificationData> emailVerificationData=new ConcurrentHashMap<>();


    public void sendMail(String mail){
        int verificationNumber = createValidationNumber();
        MimeMessage message = CreateMail(mail, verificationNumber);
        javaMailSender.send(message);

        // 인증 번호 및 시간 저장
        emailVerificationData.put(mail, new VerificationData(verificationNumber, LocalDateTime.now()));
    }

    public int createValidationNumber(){
        return (int)(Math.random()*(90000))+100000;
    }

    private MimeMessage CreateMail(String mail, int verificationNumber){
        MimeMessage message=javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO,mail);
            message.setSubject("이메일 인증");
            String body="";

            body+="<h3>"+"요청하신 이메일 인증 번호입니다."+"</h3>";
            body+="<h1>"+verificationNumber+"</h1>";
            body+="<h3>"+"감사합니다."+"</h3>";
            message.setText(body,"UTF-8","html");
        }catch (MessagingException e){
            e.printStackTrace();
        }
        return message;
    }

    //인증 번호 확인
    public boolean verifyVerificationCode(String mail,String number){
        VerificationData data = emailVerificationData.get(mail);
        if (data == null) {
            throw new RestApiException(MemberErrorCode.INVALID_MAIL_NUMBER);
        }

        // 인증 시간 초과(3분)
        if (Duration.between(data.getSentTime(), LocalDateTime.now()).toMinutes() > 3) {
            throw new RestApiException(MemberErrorCode.TIMEOUT_MAIL_NUMBER);
        }

        return Integer.toString(data.getVerificationNumber()).equals(number);
    }
    // 인증 번호 및 발송 시간 정보
    private static class VerificationData {
        private final int verificationNumber;
        private final LocalDateTime sentTime;

        public VerificationData(int verificationNumber, LocalDateTime sentTime) {
            this.verificationNumber = verificationNumber;
            this.sentTime = sentTime;
        }

        public int getVerificationNumber() {
            return verificationNumber;
        }

        public LocalDateTime getSentTime() {
            return sentTime;
        }
    }
}
