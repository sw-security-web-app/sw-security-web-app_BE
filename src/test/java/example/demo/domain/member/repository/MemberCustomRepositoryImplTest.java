package example.demo.domain.member.repository;

import example.demo.domain.company.Company;
import example.demo.domain.company.dto.response.CompanyResponseDto;
import example.demo.domain.company.repository.CompanyRepository;
import example.demo.domain.member.Member;
import example.demo.domain.member.dto.request.MemberRequestDto;
import example.demo.domain.member.dto.response.CompanyEmployeeResponseDto;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class MemberCustomRepositoryImplTest {
    @Autowired
    @Qualifier("memberCustomRepositoryImpl")
    private MemberCustomRepository memberCustomRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private EntityManager em;

    public void initMemberOfGeneral(){
        MemberRequestDto general1=MemberRequestDto.ofGeneral(
                "tkv99@naver.com", "김김김", "rlaehdus00!!", "01050299737","GENERAL"
        );
        Member member1=Member.createGeneral(general1);

        MemberRequestDto general2=MemberRequestDto.ofGeneral(
                "tkv99@naver.com", "김김김", "rlaehdus00!!", "01050299737","GENERAL"
        );
        Member member2=Member.createGeneral(general2);

        MemberRequestDto general3=MemberRequestDto.ofGeneral(
                "tkv99@naver.com", "김김김", "rlaehdus00!!", "01050299737","GENERAL"
        );
        Member member3=Member.createGeneral(general3);
        memberRepository.saveAll(List.of(member1,member2,member3));
    }

    @AfterEach
    void teardown(){
        memberRepository.deleteAllInBatch();
    }
    @Test
    @DisplayName("같은 이메일의 개수를 반환하며 존재하지 않는다면 0L을 반환합니다.")
    void getSameEmailCount() {
        //given
        initMemberOfGeneral();

        //when
        Long count=memberCustomRepository.getSameEmailCount("tkv99@naver.com");
        Long NaNCount=memberCustomRepository.getSameEmailCount("ttt@naver.com");

        //then
        assertThat(count).isEqualTo(3L);
        assertThat(NaNCount).isEqualTo(0L);
    }

    @Test
    @DisplayName("같은 휴대폰 번호의 개수를 반횐하며 존재하지 않는다면 0L을 반환합니다.")
    void getPhoneNumberCount() {
        //given
        initMemberOfGeneral();
        //when
        Long count=memberCustomRepository.getPhoneNumberCount("01050299737");
        Long NaNCount=memberCustomRepository.getPhoneNumberCount("01012345678");

        //then
        assertThat(count).isEqualTo(3L);
        assertThat(NaNCount).isEqualTo(0L);
    }

    @Test
    @DisplayName("회사id로 해당 회사 정보를 불러옵니다.")
    void getCompanyInfo(){
        //given
        String COMPANY_CODE="TEST_CODE";
        Company company1=Company
                .builder()
                .companyDept("AI1")
                .companyName("SK1")
                .invitationCode(COMPANY_CODE)
                .build();
        Company company2=Company
                .builder()
                .companyDept("AI2")
                .companyName("SK2")
                .invitationCode(COMPANY_CODE)
                .build();
        companyRepository.saveAll(List.of(company1,company2));

        MemberRequestDto memberRequestDto1=MemberRequestDto.ofManager(
                "tkv123@naver.com","김도연1","abcd123!","01012345678","SK1","AI1","사장1","MANAGER"
        );
        MemberRequestDto memberRequestDto2=MemberRequestDto.ofManager(
                "tkv124@naver.com","김도연2","abcd123!1","01012345679","SK1","AI1","사장2","MANAGER"
        );

        MemberRequestDto memberRequestDto3=MemberRequestDto.ofManager(
                "tkv125@naver.com","김도연3","abcd123!2","01012345670","SK2","AI2","사장3","MANAGER"
        );

        Member member1=Member.createManager(memberRequestDto1,company1);
        Member member2=Member.createManager(memberRequestDto2,company1);
        Member member3=Member.createManager(memberRequestDto3,company2);
        member1=memberRepository.save(member1);
        member2=memberRepository.save(member2);
        member3=memberRepository.save(member3);
        em.flush();
        em.clear();

        //when
        CompanyResponseDto companyResponseDto1=companyRepository.getCompanyInfo(member1.getMemberId());
        CompanyResponseDto companyResponseDto2=companyRepository.getCompanyInfo(member2.getMemberId());
        CompanyResponseDto companyResponseDto3=companyRepository.getCompanyInfo(member3.getMemberId());

        //then
        //직원 검증
        assertThat(companyResponseDto1.getCompanyName()).isEqualTo(company1.getCompanyName());
        assertThat(companyResponseDto1.getCompanyDept()).isEqualTo(company1.getCompanyDept());

        assertThat(companyResponseDto2.getCompanyName()).isEqualTo(company1.getCompanyName());
        assertThat(companyResponseDto2.getCompanyDept()).isEqualTo(company1.getCompanyDept());

        assertThat(companyResponseDto3.getCompanyName()).isEqualTo(company2.getCompanyName());
        assertThat(companyResponseDto3.getCompanyDept()).isEqualTo(company2.getCompanyDept());
    }
}