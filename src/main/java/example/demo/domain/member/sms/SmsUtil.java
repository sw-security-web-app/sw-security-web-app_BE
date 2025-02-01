package example.demo.domain.member.sms;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Random;

@Component
public class SmsUtil {
    @Value("${coolsms.api.key}")
    private String apiKey;
    @Value("${coolsms.api.secret}")
    private String apiSecretKey;
    @Value("${coolsms.api.senderNumber}")
    private String sendNumber;

    private DefaultMessageService messageService;

    @PostConstruct
    private void init(){
        System.out.println();
        this.messageService= NurigoApp.INSTANCE.initialize(apiKey,apiSecretKey,"https://api.coolsms.co.kr");
    }

    public SingleMessageSentResponse sendOne(String to,String verificationCode){
        Message message=new Message();
        message.setFrom(sendNumber);
        message.setTo(to);

        message.setText("[Vero AI]\n아래의 휴대폰 인증번호를 입력해주세요\n✅ "
                + verificationCode + " ✅\n(5분 내 입력해 주세요)");
        SingleMessageSentResponse response=this.messageService.sendOne(new SingleMessageSendingRequest(message));
        return response;
    }

    //랜덤 숫자 발행
    public String createRandomNumber(){
        Random random=new Random();
        String randomNum="";
        for (int i=0;i<6;i++){
            String rand=Integer.toString(random.nextInt(10));
            randomNum+=rand;
        }
        return randomNum;
    }
}
