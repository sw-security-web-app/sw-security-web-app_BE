package example.demo.domain.member.mail;

import example.demo.data.RedisCustomServiceImpl;
import example.demo.domain.member.MemberErrorCode;
import example.demo.error.RestApiException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;
    private final RedisCustomServiceImpl redisCustomService;

    //인증 메일 전송
    @PostMapping("/api/mailsend")
    public ResponseEntity<?> mailSend(@RequestParam String email){
         mailService.sendMail(email);
         return ResponseEntity.ok("인증 번호 전송");
    }

    //인증번호 일치 확인
    @GetMapping("/api/mailcheck")
    public ResponseEntity<?>mailCheck(@RequestParam String email,@RequestParam String userNumber){
        //인증 번호 일치 여부
        boolean isMatch=mailService.verifyVerificationCode(email, userNumber);
        if (!isMatch){
            return ResponseEntity.status(400).body("인증 실패");
        }else{
            //유효시간 10분 설정.
            String PREFIX = "cer: ";
            redisCustomService.saveRedisData(PREFIX +email,"TRUE", (long) (10*60));
            return  ResponseEntity.ok("인증 성공");
        }
    }
}
