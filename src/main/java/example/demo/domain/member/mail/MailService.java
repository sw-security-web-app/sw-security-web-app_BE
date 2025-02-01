package example.demo.domain.member.mail;

public interface MailService {
    void sendMail(String mail);
    int createValidationNumber();
    boolean verifyVerificationCode(String mail,String number);

}
