package example.demo.domain.member.mail;

import example.demo.domain.member.dto.request.MemberPhoneAndEmailRequestDto;

public interface MailService {
    void sendMail(String mail);
    boolean verifyVerificationCode(String mail,String number);
    void sendTemporaryPassword(MemberPhoneAndEmailRequestDto requestDto);

}
