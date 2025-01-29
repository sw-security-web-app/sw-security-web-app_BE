package example.demo.domain.member.dto.request;

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

import static org.assertj.core.api.Assertions.*;
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
    @DisplayName("회원가입 시 해당 유저 유형에 맞는 생성자를 통해 빌드합니다.")
    void signupWithMemberStatus(){
        //given  //when
        MemberRequestDto general= MemberRequestDto.builder()
                        .email("tkv00@naver.com")
                        .phoneNumber("01050299737")
                        .password("rlaehdus00!!")
                        .name("member1")
                        .build();
        MemberRequestDto manager=MemberRequestDto.builder()
                .email("tkv11@naver.com")
                .phoneNumber("01050299999")
                .password("rlaehdus11!!")
                .name("member2")


        //then
        assertThat(general).extracting(
                tuple("email","tkv00@naver.com"),
                tuple()
        )
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