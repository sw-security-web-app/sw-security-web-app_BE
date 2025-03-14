package example.demo.domain.member.dto.request;

import example.demo.domain.member.MemberStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberRequestDtoTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @Autowired
    private BCryptPasswordEncoder encoder;
    @BeforeAll
    static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void close() {
        factory.close();
    }


    @Test
    @DisplayName("회원가입 시 해당 유저 유형에 맞는 생성자를 통해 빌드합니다.")
    void signupWithMemberStatus(){
        //given  //when
        MemberRequestDto general= MemberRequestDto.ofGeneral(
                "tkv00@naver.com", "member1", "rlaehdus00!!", "01050299737","GENERAL"
        );
        MemberRequestDto manager=MemberRequestDto.ofManager(
                "tkv11@naver.com", "member2", "rlaehdus11!!", "01050299999",
                "company", "development", "사장","MANAGER"
        );
        MemberRequestDto employee=MemberRequestDto.ofEmployee(
                "tkv11@naver.com", "member2", "rlaehdus11!!", "01050299999",
                "인턴", "abcdef","EMPLOYEE"
        );

        //then
        assertThat(general)
                .extracting("email","name","password","phoneNumber","memberStatus")
                .containsExactly("tkv00@naver.com", "member1", encoder.encode("rlaehdus00!!"), "01050299737", "GENERAL");

        assertThat(manager)
                .extracting("email","name","password","phoneNumber","companyName","companyDept","companyPosition","memberStatus")
                .containsExactly("tkv11@naver.com", "member2", encoder.encode("rlaehdus11!!"), "01050299999",
                        "company", "development", "사장","MANAGER");

        assertThat(employee)
                .extracting("email","name","password","phoneNumber","companyPosition","invitationCode","memberStatus")
                .containsExactly("tkv11@naver.com", "member2", encoder.encode("rlaehdus11!!"), "01050299999",
                        "인턴", "abcdef","EMPLOYEE");
    }

    @Test
    @DisplayName("회원가입 시 이메일 유효성 검사를 실시합니다.")
    void validationEmail(){
        //given
        MemberRequestDto general1=MemberRequestDto.ofGeneral(
                null, "member1", "rlaehdus00!!", "01050299737","GENERAL"
        );
        MemberRequestDto general2=MemberRequestDto.ofGeneral(
                "", "member1", "rlaehdus00!!", "01050299737","GENERAL"
        );
        MemberRequestDto general3=MemberRequestDto.ofGeneral(
                " ", "member1", "rlaehdus00!!", "01050299737","GENERAL"
        );
        MemberRequestDto general4=MemberRequestDto.ofGeneral(
                "tkv99@naver,com", "member1", encoder.encode("rlaehdus00!!"), "01050299737","GENERAL"
        );
        MemberRequestDto general5=MemberRequestDto.ofGeneral(
                "tkv99@naver.com", "member1", encoder.encode("rlaehdus00!!"), "01050299737","GENERAL"
        );

        //when
        Set<ConstraintViolation<MemberRequestDto>> violations1 = getConstraintViolations(general1);
        Set<ConstraintViolation<MemberRequestDto>> violations2 = getConstraintViolations(general2);
        Set<ConstraintViolation<MemberRequestDto>> violations3 = getConstraintViolations(general3);
        Set<ConstraintViolation<MemberRequestDto>> violations4 = getConstraintViolations(general4);
        Set<ConstraintViolation<MemberRequestDto>> violations5 = getConstraintViolations(general5);


        violations1.forEach(error ->
                assertThat(error.getMessage()).isEqualTo("이메일은 필수 입력 값입니다.")
        );
        violations2.forEach(error ->
                assertThat(error.getMessage()).isEqualTo("이메일은 필수 입력 값입니다.")
        );
        violations3.forEach(error ->
                assertThat(error.getMessage()).isEqualTo("이메일은 필수 입력 값입니다.")
        );
        violations4.forEach(error->
                assertThat(error.getMessage()).isEqualTo("잘못된 이메일 입력입니다.")
        );
        violations5.forEach(error->
                assertThat(error.getMessage()).isEmpty()
        );
    }

    @Test
    @DisplayName("회원가입 시 이름 유효성 검사를 실시합니다.")
    void validationName(){
        //given
        MemberRequestDto general1=MemberRequestDto.ofGeneral(
                "tkv99@naver.com", null, "rlaehdus00!!", "01050299737","GENERAL"
        );
        MemberRequestDto general2=MemberRequestDto.ofGeneral(
                "tkv99@naver.com", "", "rlaehdus00!!", "01050299737","GENERAL"
        );
        MemberRequestDto general3=MemberRequestDto.ofGeneral(
                "tkv99@naver.com", " ", "rlaehdus00!!", "01050299737","GENERAL"
        );
        MemberRequestDto general4=MemberRequestDto.ofGeneral(
                "tkv99@naver.com", "김도연", "rlaehdus00!!", "01050299737","GENERAL"
        );

        //when
        Set<ConstraintViolation<MemberRequestDto>> violations1 = getConstraintViolations(general1);
        Set<ConstraintViolation<MemberRequestDto>> violations2 = getConstraintViolations(general2);
        Set<ConstraintViolation<MemberRequestDto>> violations3 = getConstraintViolations(general3);
        Set<ConstraintViolation<MemberRequestDto>> violations4 = getConstraintViolations(general4);


        violations1.forEach(error ->
                assertThat(error.getMessage()).isEqualTo("이름은 필수 입력 값입니다.")
        );
        violations2.forEach(error ->
                assertThat(error.getMessage()).isEqualTo("이름은 필수 입력 값입니다.")
        );
        violations3.forEach(error ->
                assertThat(error.getMessage()).isEqualTo("이름은 필수 입력 값입니다.")
        );
        violations4.forEach(error->
                assertThat(error.getMessage()).isEmpty()
        );
    }

    @Test
    @DisplayName("회원가입 시 비밀번호 유효성 검사를 실시합니다.")
    void validationPassword(){
        //given
        MemberRequestDto general1=MemberRequestDto.ofGeneral(
                "tkv00@naver.com", "member1", null, "01050299737","GENERAL"
        );
        MemberRequestDto general2=MemberRequestDto.ofGeneral(
                "tkv00@naver.com", "member1", "", "01050299737","GENERAL"
        );
        MemberRequestDto general3=MemberRequestDto.ofGeneral(
                "tkv00@naver.com", "member1", " ", "01050299737","GENERAL"
        );
        //특수문자 사용X
        MemberRequestDto general4=MemberRequestDto.ofGeneral(
                "tkv00@naver.com", "member1", "rlaehdus00", "01050299737","GENERAL"
        );
        //8자미만
        MemberRequestDto general5=MemberRequestDto.ofGeneral(
                "tkv00@naver.com", "member1", "rlaehd", "01050299737","GENERAL"
        );
        //20자초과
        MemberRequestDto general6=MemberRequestDto.ofGeneral(
                "tkv00@naver.com", "member1", "rlaehdus00111111111111111111111111111111", "01050299737","GENERAL"
        );
        //영어 사용X 혹은 한글 포함
        MemberRequestDto general7=MemberRequestDto.ofGeneral(
                "tkv00@naver.com", "member1", "rlaehㅁㄴㅇㅁㄴㅇdus00", "01050299737","GENERAL"
        );
        //올바른 값
        MemberRequestDto general8=MemberRequestDto.ofGeneral(
                "tkv00@naver.com", "member1", "avsdsfsd00!!", "01050299737","GENERAL"
        );


        //when
        Set<ConstraintViolation<MemberRequestDto>> violations1 = getConstraintViolations(general1);
        Set<ConstraintViolation<MemberRequestDto>> violations2 = getConstraintViolations(general2);
        Set<ConstraintViolation<MemberRequestDto>> violations3 = getConstraintViolations(general3);
        Set<ConstraintViolation<MemberRequestDto>> violations4 = getConstraintViolations(general4);
        Set<ConstraintViolation<MemberRequestDto>> violations5 = getConstraintViolations(general5);
        Set<ConstraintViolation<MemberRequestDto>> violations6 = getConstraintViolations(general6);
        Set<ConstraintViolation<MemberRequestDto>> violations7 = getConstraintViolations(general7);
        Set<ConstraintViolation<MemberRequestDto>> violations8 = getConstraintViolations(general8);


        violations1.forEach(error ->
                assertThat(error.getMessage()).isEqualTo("비밀번호는 필수 입력 값입니다.")
        );
        violations2.forEach(error ->
                assertThat(error.getMessage()).isEqualTo("비밀번호는 필수 입력 값입니다.")
        );
        violations3.forEach(error ->
                assertThat(error.getMessage()).isEqualTo("비밀번호는 필수 입력 값입니다.")
        );
        violations4.forEach(error->
                assertThat(error.getMessage()).isEqualTo("잘못된 비밀번호 형식입니다.")
        );
        violations5.forEach(error->
                assertThat(error.getMessage()).isEqualTo("잘못된 비밀번호 형식입니다.")
        );
        violations6.forEach(error->
                assertThat(error.getMessage()).isEqualTo("잘못된 비밀번호 형식입니다.")
        );
        violations7.forEach(error->
                assertThat(error.getMessage()).isEqualTo("잘못된 비밀번호 형식입니다.")
        );
        violations8.forEach(error->
                assertThat(error.getMessage()).isEmpty()
        );
    }

    @Test
    @DisplayName("회원가입 시 휴대폰번호 유효성 검사를 실시합니다.")
    void validationPhoneNumber(){
        //given
        MemberRequestDto general1=MemberRequestDto.ofGeneral(
                "tkv00@naver.com", "member1", "rlaehdus00!!", null,"GENERAL"
        );
        MemberRequestDto general2=MemberRequestDto.ofGeneral(
                "tkv00@naver.com", "member1", "rlaehdus00!!", "","GENERAL"
        );
        MemberRequestDto general3=MemberRequestDto.ofGeneral(
                "tkv00@naver.com", "member1", "rlaehdus00!!",  " ","GENERAL"
        );
        //11자미만
        MemberRequestDto general4=MemberRequestDto.ofGeneral(
                "tkv00@naver.com", "member1", "rlaehdus00!!",  "0100000000","GENERAL"
        );
        //11자초과
        MemberRequestDto general5=MemberRequestDto.ofGeneral(
                "tkv00@naver.com", "member1", "rlaehdus00!!",  "010502997371","GENERAL"
        );
        //하이픈 첨가
        MemberRequestDto general6=MemberRequestDto.ofGeneral(
                "tkv00@naver.com", "member1", "rlaehdus00!!", "010-5029-9737","GENERAL"
        );
        //올바른 값 입력
        MemberRequestDto general7=MemberRequestDto.ofGeneral(
                "tkv00@naver.com", "member1", "rlaehdus00!!", "01050299737","GENERAL"
        );


        //when
        Set<ConstraintViolation<MemberRequestDto>> violations1 = getConstraintViolations(general1);
        Set<ConstraintViolation<MemberRequestDto>> violations2 = getConstraintViolations(general2);
        Set<ConstraintViolation<MemberRequestDto>> violations3 = getConstraintViolations(general3);
        Set<ConstraintViolation<MemberRequestDto>> violations4 = getConstraintViolations(general4);
        Set<ConstraintViolation<MemberRequestDto>> violations5 = getConstraintViolations(general5);
        Set<ConstraintViolation<MemberRequestDto>> violations6 = getConstraintViolations(general6);
        Set<ConstraintViolation<MemberRequestDto>> violations7 = getConstraintViolations(general7);


        violations1.forEach(error ->
                assertThat(error.getMessage()).isEqualTo("휴대폰 번호는 필수 입력 값입니다.")
        );
        violations2.forEach(error ->
                assertThat(error.getMessage()).isEqualTo("휴대폰 번호는 필수 입력 값입니다.")
        );
        violations3.forEach(error ->
                assertThat(error.getMessage()).isEqualTo("휴대폰 번호는 필수 입력 값입니다.")
        );
        violations4.forEach(error->
                assertThat(error.getMessage()).isEqualTo("잘못된 휴대폰 번호 형식입니다.")
        );
        violations5.forEach(error->
                assertThat(error.getMessage()).isEqualTo("잘못된 휴대폰 번호 형식입니다.")
        );
        violations6.forEach(error->
                assertThat(error.getMessage()).isEqualTo("잘못된 휴대폰 번호 형식입니다.")
        );
        violations7.forEach(error->
                assertThat(error.getMessage()).isEmpty()
        );

    }
    private static Set<ConstraintViolation<MemberRequestDto>> getConstraintViolations(MemberRequestDto general1) {
        Set<ConstraintViolation<MemberRequestDto>> violations=validator.validate(general1);
        return violations;
    }
}