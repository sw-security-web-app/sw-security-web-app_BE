package example.demo.domain.member.dto.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class MemberRequestDtoTest {

    @Autowired
    private static ValidatorFactory validatorFactory;
    @Autowired
    private static Validator validator;

    @BeforeAll
    public static void init(){
        validatorFactory= Validation.buildDefaultValidatorFactory();
        validator=validatorFactory.getValidator();
    }

    @AfterAll
    public static void close(){
        validatorFactory.close();
    }

    @Test
    @DisplayName("회원가입 시 이메일 유효성 검사를 실시합니다.")
    void validationEmail(){
        //given
       // MemberRequestDto memberRequestDto=MemberRequestDto;
        //when

        //then
    }
}