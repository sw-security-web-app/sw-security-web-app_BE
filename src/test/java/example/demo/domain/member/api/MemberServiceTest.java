package example.demo.domain.member.api;

import example.demo.domain.company.Company;
import example.demo.domain.company.repository.CompanyRepository;
import example.demo.domain.company.dto.CompanyCodeDto;
import example.demo.domain.member.Member;
import example.demo.domain.member.MemberErrorCode;
import example.demo.domain.member.repository.MemberRepository;
import example.demo.domain.member.dto.request.MemberRequestDto;
import example.demo.error.RestApiException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static example.demo.domain.member.MemberStatus.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
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
    @Autowired
    private PasswordEncoder encoder;

    @BeforeAll
    static void setup() {
        System.setProperty("test.mode", "true");
    }
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
        assertThat(findMember1).extracting("email","userName","phoneNumber","memberStatus")
                .containsExactly("tkv00@naver.com","member1","01012345678", GENERAL);
        assertThat(encoder.matches("rlaehdus00!!",findMember1.getPassword())).isTrue();

        assertThat(findMember2).extracting("email","userName","phoneNumber","memberStatus")
                .containsExactly("tkv11@naver.com","member2","01012345611",GENERAL);
        assertThat(encoder.matches("rlaehdus00!!",findMember2.getPassword())).isTrue();

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
        assertThat(findMember1).extracting("email","userName","phoneNumber","memberStatus","companyPosition")
                .containsExactly("tkv00@naver.com","member1","01012345678",MANAGER,"사장");
        assertThat(encoder.matches("rlaehdus00!!",findMember1.getPassword())).isTrue();

        assertThat(findCompany1).extracting("companyName","companyDept")
                        .containsExactly("삼성","개발");

        assertThat(findMember2).extracting("email","userName","phoneNumber","memberStatus","companyPosition")
                .containsExactly("tkv11@naver.com","member2","01012345611",MANAGER,"부사장");
        assertThat(encoder.matches("rlaehdus00!!",findMember2.getPassword())).isTrue();

        assertThat(findCompany2).extracting("companyName","companyDept")
                .containsExactly("LG","디스플레이");
    }

    @Test
    @DisplayName("회사 직원이 회원가입을 실시합니다.")
    void signUpEmployee() {
        //given
        //관리자 생성
        MemberRequestDto manager1dto=MemberRequestDto
                .ofManager("tkv00@naver.com","member1","rlaehdus00!!","01012345678","삼성","개발","사장","MANAGER");
        MemberRequestDto manager2dto=MemberRequestDto
                .ofManager("tkv11@naver.com","member2","rlaehdus00!!","01012345611","LG","디스플레이","부사장","MANAGER");
        memberService.signup(manager1dto);
        memberService.signup(manager2dto);
        em.flush();
        Member manager1=memberRepository.findByEmailAndPhoneNumber(manager1dto.getEmail(), manager1dto.getPhoneNumber())
                .orElseThrow(()->new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));
        Member manager2=memberRepository.findByEmailAndPhoneNumber(manager2dto.getEmail(), manager2dto.getPhoneNumber())
                .orElseThrow(()->new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));
        em.flush();

        CompanyCodeDto companyCode1=companyRepository.findCompanyCode(manager1.getMemberId());
        CompanyCodeDto companyCode2=companyRepository.findCompanyCode(manager2.getMemberId());
        //직원 생성
        MemberRequestDto employee1=MemberRequestDto
                .ofEmployee("tkv00222@naver.com","employee1","rlaehdus00!!","01012345622","인턴1",companyCode1.getCompanyCode(),"EMPLOYEE");
        MemberRequestDto employee2=MemberRequestDto
                .ofEmployee("tkv1131@naver.com","employee2","rlaehdus00!!","01012345633","인턴2",companyCode2.getCompanyCode(),"EMPLOYEE");

        //when
        memberService.signup(employee1);
        memberService.signup(employee2);


        Member findMember1=memberRepository.findByEmailAndPhoneNumber(employee1.getEmail(), employee1.getPhoneNumber())
                .orElseThrow(()->new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));
        Member findMember2=memberRepository.findByEmailAndPhoneNumber(employee2.getEmail(), employee2.getPhoneNumber())
                .orElseThrow(()->new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));
        Company findCompany1=findMember1.getCompany();
        Company findCompany2=findMember2.getCompany();

        //then
        assertThat(findMember1).extracting("email","userName","phoneNumber","companyPosition","memberStatus")
                .containsExactly("tkv00222@naver.com","employee1","01012345622","인턴1",EMPLOYEE);
        assertThat(encoder.matches("rlaehdus00!!",findMember1.getPassword())).isTrue();
        assertThat(findCompany1).extracting("companyName","companyDept")
                .containsExactly("삼성","개발");

        assertThat(findMember2).extracting("email","userName","phoneNumber","companyPosition","memberStatus")
                .containsExactly("tkv1131@naver.com","employee2","01012345633","인턴2",EMPLOYEE);
        assertThat(encoder.matches("rlaehdus00!!",findMember2.getPassword())).isTrue();
        assertThat(findCompany2).extracting("companyName","companyDept")
                .containsExactly("LG","디스플레이");
    }

}
