package example.demo.domain.member.sms;

import example.demo.data.RedisCustomServiceImpl;
import example.demo.domain.member.MemberErrorCode;
import example.demo.domain.member.dto.request.SmsCertificationRequestDto;
import example.demo.error.RestApiException;
import example.demo.util.CreateRandom;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

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


    @PostConstruct
    private void init(){
        this.messageService= NurigoApp.INSTANCE.initialize(apiKey,apiSecretKey,"https://api.coolsms.co.kr");
    }
    @Override
    public SingleMessageSentResponse sendOne(String to, String verificationCode) {
        Message message=new Message();
        message.setFrom(sendNumber);
        message.setTo(to);

        message.setText("[Vero AI]\n아래의 휴대폰 인증번호를 입력해주세요\n✅ "
                + verificationCode + " ✅\n(5분 내 입력해 주세요)");
        SingleMessageSentResponse response=this.messageService.sendOne(new SingleMessageSendingRequest(message));
        return response;
    }

    @Override
    public void sendSms(SmsCertificationRequestDto smsCertificationRequestDto) {
        String to=smsCertificationRequestDto.getPhoneNumber();
        String random= CreateRandom.createRandomNumber();
        String SMS_PREFIX="sms: ";
        smsUtil.sendOne(to,random);
        redisCustomService.saveRedisData(SMS_PREFIX+to,smsCertificationRequestDto.getCertificationCode(), 5L*60);
    }


}
