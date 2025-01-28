package example.demo.domain.member.mail;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;
    private int number;//이메일 인증 저장 number

    //인증 메일 전송
    @PostMapping("/api/mailsend")
    public HashMap<String, Object>mailSend(String mail){
        HashMap<String,Object> map=new HashMap<>();

        try {
            number= mailService.sendMail(mail);
            String num=String.valueOf(number);

            map.put("success",Boolean.TRUE);
            map.put("number",num);
        }catch (Exception e){
            map.put("success",Boolean.FALSE);
            map.put("error",e.getMessage());
        }
        return map;
    }

    //인증번호 일치 확인
    @GetMapping("/api/mailcheck")
    public ResponseEntity<?>mailCheck(@RequestParam String userNumber){
        boolean isMatch=userNumber.equals(String.valueOf(number));
        return ResponseEntity.ok(isMatch);
    }
}
