package example.demo.domain.member.dto.request;

import example.demo.domain.member.MemberStatus;
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
        MemberRequestDto general= MemberRequestDto.ofGeneral(
                "tkv00@naver.com", "member1", "rlaehdus00!!", "01050299737"
        );
        MemberRequestDto manager=MemberRequestDto.ofManager(
                "tkv11@naver.com", "member2", "rlaehdus11!!", "01050299999",
                "company", "development", "사장"
        );
        MemberRequestDto employee=MemberRequestDto.ofEmployee(
                "tkv11@naver.com", "member2", "rlaehdus11!!", "01050299999",
                "인턴", "abcdef"
        );

        //then
        assertThat(general)
                .extracting("email","name","password","phoneNumber","memberStatus")
                .containsExactly("tkv00@naver.com", "member1", "rlaehdus00!!", "01050299737", MemberStatus.GENERAL);

        assertThat(manager)
                .extracting("email","name","password","phoneNumber","companyName","companyDept","companyPosition","memberStatus")
                .containsExactly("tkv11@naver.com", "member2", "rlaehdus11!!", "01050299999",
                        "company", "development", "사장",MemberStatus.MANAGER);

        assertThat(employee)
                .extracting("email","name","password","phoneNumber","companyPosition","invitationCode","memberStatus")
                .containsExactly("tkv11@naver.com", "member2", "rlaehdus11!!", "01050299999",
                        "인턴", "abcdef",MemberStatus.EMPLOYEE);
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