package example.demo.domain.member.api;

import example.demo.domain.company.Company;
import example.demo.domain.member.Member;
import example.demo.domain.member.MemberRepository;
import example.demo.domain.member.MemberStatus;
import example.demo.domain.member.dto.request.MemberRequestDto;
import example.demo.util.CreateUuid;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class MemberServiceTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("사용자가 회원가입을 실시합니다.")
    void signUp() {
        //given
        //일반인
        MemberRequestDto generalDto=MemberRequestDto
                .ofGeneral("tkv00@naver.com","member1","rlaehdus00!!","01012345678");

        //관리자
        MemberRequestDto managerDto=MemberRequestDto
                .ofManager("tkv00@naver.com","member2","rlaehdus00!!","01012345678","company1","개발","사장");

        //직원
        MemberRequestDto employeeDto=MemberRequestDto
                .ofEmployee("tkv00@naver.com","member3","rlaehdus00!!","01012345678","인턴","");

        //when

        //then
    }
}