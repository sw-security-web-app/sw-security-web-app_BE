package example.demo.domain.member.sms;
import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


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

    public void sendOne(String to, String verificationCode){
        getResponse(to, "[Vero AI]\n아래의 휴대폰 인증번호를 입력해주세요\n✅ "
                + verificationCode + " ✅\n(5분 내 입력해 주세요)");
    }

    //휴대폰 번호로 가입한 이메일 정보 전송
    public void sendMemberEmail(String to, String email){
        getResponse(to, "[Vero AI]\n회원님이 가입하신 이메일은 다음과 같습니다.\n" + email);
    }
    @Nullable
    private SingleMessageSentResponse getResponse(String to, String verificationCode) {
        Message message = new Message();
        message.setFrom(sendNumber);
        message.setTo(to);

        message.setText(verificationCode);
        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        return response;
    }

}
