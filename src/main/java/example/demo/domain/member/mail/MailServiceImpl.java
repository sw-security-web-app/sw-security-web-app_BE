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
    private static final String senderEmail="tkv0098@gmail.com";
    private static final String MAIL_PREFIX="mail: ";

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
        redisCustomService.saveRedisData(MAIL_PREFIX+mail,verificationNumber,5L*60);
    }


    //임시 비밀번호 전송
    @Override
    public void sendTemporaryPassword(MemberPhoneAndEmailRequestDto requestDto) {
        //휴대폰 인증을 안한 경우
        if(smsValidation(requestDto.getPhoneNumber())){
            throw new RestApiException(MemberErrorCode.INVALID_PHONE_CERTIFICATION_NUMBER);
        }
        //유저의 이메일
        javaMailSender.send(createTemporaryPassword(requestDto.getEmail()));
    }
    @Override
    //인증 번호 확인
    public boolean verifyVerificationCode(String mail,String number){

        String verificationNumber = redisCustomService.getRedisData(MAIL_PREFIX+mail);
        if (verificationNumber == null) {
            throw new RestApiException(MemberErrorCode.INVALID_MAIL_NUMBER);
        }


        return verificationNumber.equals(number);
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
        String uuid=CreateRandom.createNewPassword();
        findMember.setPassword(passwordEncoder.encode(uuid));
        memberRepository.save(findMember);
        String body="<h3>"+"임시 비밀번호입니다."+"</h3>"+
                "<h1>"+uuid+"</h1>"+
                "<h3>"+"감사합니다."+"</h3>";
        return createMailMessage(mail,subject,body);
    }

    // 인증 번호 및 발송 시간 정보

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
