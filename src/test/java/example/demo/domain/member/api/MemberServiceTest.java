package example.demo.domain.member.api;

import example.demo.domain.company.Company;
import example.demo.domain.member.Member;
import example.demo.domain.member.MemberErrorCode;
import example.demo.domain.member.MemberRepository;
import example.demo.domain.member.MemberStatus;
import example.demo.domain.member.dto.request.MemberRequestDto;
import example.demo.error.RestApiException;
import example.demo.util.CreateUuid;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class MemberServiceTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("일반 사용자가 회원가입을 실시합니다.")
    void signUp() {
        //given
        //일반인
        MemberRequestDto generalDto1=MemberRequestDto
                .ofGeneral("tkv00@naver.com","member1","rlaehdus00!!","01012345678","GENERAL");
        MemberRequestDto generalDto2=MemberRequestDto
                .ofGeneral("tkv11@naver.com","member2","rlaehdus00!!","01012345611","GENERAL");
        //when
        memberService.signup(generalDto1);
        memberService.signup(generalDto2);

        Member findMember1=memberRepository.findByEmailAndPhoneNumber(generalDto1.getEmail(), generalDto1.getPhoneNumber())
                .orElseThrow(()->new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));
        Member findMember2=memberRepository.findByEmailAndPhoneNumber(generalDto2.getEmail(), generalDto2.getPhoneNumber())
                .orElseThrow(()->new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));

        //then
        assertThat(findMember1).extracting("email","userName","password","phoneNumber")
                .containsExactly("tkv00@naver.com","member1","rlaehdus00!!","01012345678");
        assertThat(findMember2).extracting("email","userName","password","phoneNumber")
                .containsExactly("tkv11@naver.com","member2","rlaehdus00!!","01012345611");

    }

/*
    //관리자
    MemberRequestDto managerDto=MemberRequestDto
            .ofManager("tkv00@naver.com","member2","rlaehdus00!!","01012345678","company1","개발","사장");

    //직원
    MemberRequestDto employeeDto=MemberRequestDto
            .ofEmployee("tkv00@naver.com","member3","rlaehdus00!!","01012345678","인턴","");
*/

}