package example.demo.domain.member.mail;

import example.demo.data.RedisCustomService;
import example.demo.data.RedisCustomServiceImpl;
import example.demo.domain.member.Member;
import example.demo.domain.member.MemberErrorCode;
import example.demo.domain.member.dto.request.MemberPhoneAndEmailRequestDto;
import example.demo.domain.member.repository.MemberRepository;
import example.demo.error.RestApiException;
import example.demo.util.CreateRandom;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final JavaMailSender javaMailSender;
    private final MemberRepository memberRepository;
    private final RedisCustomService redisCustomService;
    private final PasswordEncoder passwordEncoder;
    static final String senderEmail="tkv0098@gmail.com";
    private final ConcurrentHashMap<String,VerificationData> emailVerificationData=new ConcurrentHashMap<>();

    @Override
    public void sendMail(String mail){
        String verificationNumber = CreateRandom.createRandomNumber();
        if (isDuplicatedEmail(mail)){
            throw new RestApiException(MemberErrorCode.DUPLICATED_EMAIL);
        }
         MimeMessage message = CreateMail(mail, verificationNumber);
        //이메일 중복시

        javaMailSender.send(message);

        // 인증 번호 및 시간 저장
        emailVerificationData.put(mail, new VerificationData(verificationNumber, LocalDateTime.now()));
    }


    //임시 비밀번호 전송
    @Override
    public void sendTemporaryPassword(MemberPhoneAndEmailRequestDto requestDto) {
        //휴대폰 인증을 안한 경우
//        if(smsValidation(requestDto.getPhoneNumber())){
//            throw new RestApiException(MemberErrorCode.INVALID_PHONE_CERTIFICATION_NUMBER);
//        }
        javaMailSender.send(createTemporaryPassword(requestDto.getEmail()));
    }
    @Override
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

        return data.getVerificationNumber().equals(number);
    }


    private MimeMessage CreateMail(String mail, String verificationNumber){
       String subject="[Vero AI]이메일 인증";
       String body="<h3>"+"요청하신 이메일 인증 번호입니다."+"</h3>"+
                    "<h1>"+verificationNumber+"</h1>"+
                    "<h3>"+"감사합니다."+"</h3>";

        return createMailMessage(mail,subject,body);
    }
    private MimeMessage createTemporaryPassword(String mail){
        Member findMember=memberRepository.findByEmail(mail).orElseThrow(
                ()->new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND)
        );

        String subject="[Vero AI]임시비밀번호";
        String uuid=CreateRandom.createShortUuid();
        findMember.setPassword(passwordEncoder.encode(uuid));
        memberRepository.save(findMember);
        String body="<h3>"+"임시 비밀번호입니다."+"</h3>"+
                "<h1>"+uuid+"</h1>"+
                "<h3>"+"감사합니다."+"</h3>";
        return createMailMessage(mail,subject,body);
    }

    // 인증 번호 및 발송 시간 정보
    private static class VerificationData {
        private final String verificationNumber;
        private final LocalDateTime sentTime;

        public VerificationData(String verificationNumber, LocalDateTime sentTime) {
            this.verificationNumber = verificationNumber;
            this.sentTime = sentTime;
        }

        public String getVerificationNumber() {
            return verificationNumber;
        }
        public LocalDateTime getSentTime() {
            return sentTime;
        }
    }
    //이메일 중복 검사
    private boolean isDuplicatedEmail(String email){
        //같은 이메일 1개이상 -> true 반환
        return memberRepository.getSameEmailCount(email)>0L;
    }
    //휴대폰 인증 여부 확인
    private boolean smsValidation(String phoneNumber){
        if(isTestMode()){
            return false;
        }
        String VALIDATION_PREFIX="cer: ";
        return !(redisCustomService.hasKey(VALIDATION_PREFIX+phoneNumber)&&
                    redisCustomService.getRedisData(VALIDATION_PREFIX+phoneNumber).equals("TRUE")
                );
    }

    //test를 위한 메서드
    private MimeMessage createMailMessage(String mail,String subject,String body){
        MimeMessage message=javaMailSender.createMimeMessage();
        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO,mail);
            message.setSubject(subject);
            message.setText(body,"UTF-8","html");
        }catch (MessagingException e){
            e.printStackTrace();
        }
        return message;
    }

    private boolean isTestMode() {
        return Boolean.parseBoolean(System.getProperty("test.mode", "false"));
    }
}
