package example.demo.domain.member.api;

import example.demo.domain.company.Company;
import example.demo.domain.company.CompanyRepository;
import example.demo.domain.member.Member;
import example.demo.domain.member.MemberErrorCode;
import example.demo.domain.member.MemberRepository;
import example.demo.domain.member.MemberStatus;
import example.demo.domain.member.dto.request.MemberRequestDto;
import example.demo.error.RestApiException;
import example.demo.util.CreateUuid;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static example.demo.domain.member.MemberStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private EntityManager em;
    @AfterEach
    void tearDown(){
        memberRepository.deleteAllInBatch();
        companyRepository.deleteAllInBatch();
    }
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
        assertThat(findMember1).extracting("email","userName","password","phoneNumber","memberStatus")
                .containsExactly("tkv00@naver.com","member1","rlaehdus00!!","01012345678", GENERAL);
        assertThat(findMember2).extracting("email","userName","password","phoneNumber","memberStatus")
                .containsExactly("tkv11@naver.com","member2","rlaehdus00!!","01012345611",GENERAL);

    }

    @Test
    @DisplayName("회사 관리자가 회원가입을 실시합니다.")
    void signUpManager() {
        //given
        //일반인
        MemberRequestDto manager1=MemberRequestDto
                .ofManager("tkv00@naver.com","member1","rlaehdus00!!","01012345678","삼성","개발","사장","MANAGER");
        MemberRequestDto manager2=MemberRequestDto
                .ofManager("tkv11@naver.com","member2","rlaehdus00!!","01012345611","LG","디스플레이","부사장","MANAGER");
        //when
        memberService.signup(manager1);
        memberService.signup(manager2);
        em.flush();

        Member findMember1=memberRepository.findByEmailAndPhoneNumber(manager1.getEmail(), manager1.getPhoneNumber())
                .orElseThrow(()->new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));
        Member findMember2=memberRepository.findByEmailAndPhoneNumber(manager2.getEmail(), manager2.getPhoneNumber())
                .orElseThrow(()->new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));
        Company findCompany1=findMember1.getCompany();

        Company findCompany2=findMember2.getCompany();

        //then
        assertThat(findMember1).extracting("email","userName","password","phoneNumber","memberStatus")
                .containsExactly("tkv00@naver.com","member1","rlaehdus00!!","01012345678",MANAGER);
        assertThat(findCompany1).extracting("companyName","companyDept","companyPosition")
                        .containsExactly("삼성","개발","사장");

        assertThat(findMember2).extracting("email","userName","password","phoneNumber","memberStatus")
                .containsExactly("tkv11@naver.com","member2","rlaehdus00!!","01012345611",MANAGER);
        assertThat(findCompany2).extracting("companyName","companyDept","companyPosition")
                .containsExactly("LG","디스플레이","부사장");
    }


}