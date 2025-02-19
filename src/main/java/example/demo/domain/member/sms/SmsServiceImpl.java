package example.demo.domain.member.sms;

import example.demo.data.RedisCustomServiceImpl;
import example.demo.domain.member.Member;
import example.demo.domain.member.MemberErrorCode;
import example.demo.domain.member.dto.request.SmsCertificationRequestDto;
import example.demo.domain.member.dto.response.FindEmailResponseDto;
import example.demo.domain.member.repository.MemberRepository;
import example.demo.error.RestApiException;
import example.demo.util.CreateRandom;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService{
    @Value("${coolsms.api.key}")
    private String apiKey;
    @Value("${coolsms.api.secret}")
    private String apiSecretKey;
    @Value("${coolsms.api.senderNumber}")
    private String sendNumber;

    private DefaultMessageService messageService;
    private final SmsUtil smsUtil;
    private final RedisCustomServiceImpl redisCustomService;
    private final MemberRepository memberRepository;


    @PostConstruct
    private void init(){
        this.messageService= NurigoApp.INSTANCE.initialize(apiKey,apiSecretKey,"https://api.coolsms.co.kr");
    }


    @Override
    public void sendSms(SmsCertificationRequestDto smsCertificationRequestDto) {
        String to=smsCertificationRequestDto.getPhoneNumber();
        String random= CreateRandom.createRandomNumber();
        String SMS_PREFIX="sms: ";
        smsUtil.sendOne(to,random);
        redisCustomService.saveRedisData(SMS_PREFIX+to,random, 5L*60);
    }

    //휴대폰 번호로 가입한 이메일 정보 전송
    @Override
    public void sendMemberEmail(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new RestApiException(MemberErrorCode.EMPTY_PHONENUMBER);
        }
        if (!phoneNumber.matches("^(010|011|016|017|018|019)\\d{7,8}$")){
            throw new RestApiException(MemberErrorCode.INVALID_PHONENUMBER);
        }
        Member findMember=memberRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(()->new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));
        //DTO로 반환
        FindEmailResponseDto email=FindEmailResponseDto.builder()
                .email(findMember.getEmail())
                .build();
        smsUtil.sendMemberEmail(phoneNumber, email.getEmail());
    }



}
