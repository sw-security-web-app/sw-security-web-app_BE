
package example.demo.domain.member.repository;

import example.demo.domain.company.Company;

import example.demo.domain.company.dto.response.CompanyResponseDto;
import example.demo.domain.company.repository.CompanyRepository;
import example.demo.domain.member.Member;
import example.demo.domain.member.MemberStatus;
import example.demo.domain.member.dto.request.MemberRequestDto;
import example.demo.domain.member.dto.response.CompanyEmployeeResponseDto;
import jakarta.persistence.EntityManager;

import example.demo.domain.company.repository.CompanyRepository;
import example.demo.domain.member.Member;
import example.demo.domain.member.dto.request.MemberRequestDto;
import example.demo.domain.member.dto.response.MemberInfoResponseDto;
import jakarta.transaction.Transactional;

import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
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
import org.springframework.data.domain.Sort;


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
    EntityManager em;

    @AfterEach
    void teardown() {
        memberRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("같은 이메일의 개수를 반환하며 존재하지 않는다면 0L을 반환합니다.")
    void getSameEmailCount() {
        //given

        MemberRequestDto general1 = MemberRequestDto.ofGeneral(
                "tkv99@naver.com", "김김김", "rlaehdus00!!", "01050299737", "GENERAL"
        );
        Member member1 = Member.createGeneral(general1);

        MemberRequestDto general2 = MemberRequestDto.ofGeneral(
                "tkv99@naver.com", "김김김", "rlaehdus00!!", "01050299737", "GENERAL"
        );
        Member member2 = Member.createGeneral(general2);

        MemberRequestDto general3 = MemberRequestDto.ofGeneral(
                "tkv99@naver.com", "김김김", "rlaehdus00!!", "01050299737", "GENERAL"
        );
        Member member3 = Member.createGeneral(general3);
        memberRepository.saveAll(List.of(member1, member2, member3));
    }


    @Test
    @DisplayName("같은 휴대폰 번호의 개수를 반횐하며 존재하지 않는다면 0L을 반환합니다.")
    void getPhoneNumberCount() {
        //given
        initMemberOfGeneral();
        //when
        Long count = memberCustomRepository.getPhoneNumberCount("01012345678");
        Long NaNCount = memberCustomRepository.getPhoneNumberCount("01012345679");

        //then
        assertThat(count).isEqualTo(5L);
        assertThat(NaNCount).isEqualTo(0L);
    }

    @Test
    @DisplayName("회사id로 해당 회사 정보를 불러옵니다.")
    void getCompanyInfo() {
        //given
        String COMPANY_CODE = "TEST_CODE";
        Result result = getResult(COMPANY_CODE);

        //when
        CompanyResponseDto companyResponseDto1 = companyRepository.getCompanyInfo(result.member1().getMemberId());
        CompanyResponseDto companyResponseDto2 = companyRepository.getCompanyInfo(result.member2().getMemberId());
        CompanyResponseDto companyResponseDto3 = companyRepository.getCompanyInfo(result.member3().getMemberId());

        //then
        //직원 검증
        assertThat(companyResponseDto1.getCompanyName()).isEqualTo(result.company1().getCompanyName());
        assertThat(companyResponseDto1.getCompanyDept()).isEqualTo(result.company1().getCompanyDept());

        assertThat(companyResponseDto2.getCompanyName()).isEqualTo(result.company1().getCompanyName());
        assertThat(companyResponseDto2.getCompanyDept()).isEqualTo(result.company1().getCompanyDept());

        assertThat(companyResponseDto3.getCompanyName()).isEqualTo(result.company2().getCompanyName());
        assertThat(companyResponseDto3.getCompanyDept()).isEqualTo(result.company2().getCompanyDept());
    }

    @Test
    @DisplayName("회사직원 목록을 페이징하여 조회합니다.")
    void getCompanyEmployeeInfo() {
        //given
        String COMPANY_CODE = "TEST_CODE";
        Result result = getResult(COMPANY_CODE);

        //페이징 케이스
        Pageable page1 = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "name"));
        Pageable page2 = PageRequest.of(1, 3, Sort.by(Sort.Direction.DESC, "name"));

        //when
        Page<CompanyEmployeeResponseDto> result1 = memberRepository.getCompanyEmployeeInfo(result.company1.getCompanyId(), page1);
        Page<CompanyEmployeeResponseDto> result2 = memberRepository.getCompanyEmployeeInfo(result.company1.getCompanyId(), page2);

        //then
        assertThat(result1.getTotalPages()).isEqualTo(2);
        assertThat(result1.getTotalElements()).isEqualTo(4);
        assertThat(result1.getContent().size()).isEqualTo(3);
        assertThat(result2.getContent().size()).isEqualTo(1);
    }

    @NotNull
    private Result getResult(String COMPANY_CODE) {
        Company company1 = Company
                .builder()
                .companyDept("AI1")
                .companyName("SK1")
                .invitationCode(COMPANY_CODE)
                .build();
        Company company2 = Company
                .builder()
                .companyDept("AI2")
                .companyName("SK2")
                .invitationCode(COMPANY_CODE)
                .build();
        companyRepository.saveAll(List.of(company1, company2));

        MemberRequestDto memberRequestDto1 = MemberRequestDto.ofManager(
                "tkv123@naver.com", "김도연1", "abcd123!", "01012345678", "SK1", "AI1", "사장1", "MANAGER"
        );
        MemberRequestDto memberRequestDto2 = MemberRequestDto.ofManager(
                "tkv124@naver.com", "김도연2", "abcd123!12", "01012345679", "SK1", "AI1", "사장2", "MANAGER"
        );
        MemberRequestDto memberRequestDto3 = MemberRequestDto.ofManager(
                "tkv124@naver.com", "김도연3", "abcd123!13", "01012345679", "SK1", "AI1", "사장3", "MANAGER"
        );
        MemberRequestDto memberRequestDto4 = MemberRequestDto.ofManager(
                "tkv124@naver.com", "김도연4", "abcd123!14", "01012345679", "SK1", "AI1", "사장4", "MANAGER"
        );

        MemberRequestDto memberRequestDto5 = MemberRequestDto.ofManager(
                "tkv125@naver.com", "김도연5", "abcd123!2", "01012345670", "SK2", "AI2", "사장3", "MANAGER"
        );

        Member member1 = Member.createManager(memberRequestDto1, company1);
        Member member2 = Member.createManager(memberRequestDto2, company1);
        Member member4 = Member.createManager(memberRequestDto4, company1);
        Member member5 = Member.createManager(memberRequestDto5, company1);
        Member member3 = Member.createManager(memberRequestDto3, company2);
        member1 = memberRepository.save(member1);
        member2 = memberRepository.save(member2);
        member3 = memberRepository.save(member3);
        member4 = memberRepository.save(member4);
        member5 = memberRepository.save(member5);
        em.flush();
        em.clear();
        Result result = new Result(company1, company2, member1, member2, member3, member4, member5);
        return result;
    }

    private record Result(Company company1, Company company2, Member member1, Member member2, Member member3,
                          Member member4, Member member5) {


    }

    @DisplayName("관리자 회원의 정보와 회원의 회사 정보를 가지고 옵니다.")
    void getMemberInfo() {
        //given
        MemberRequestDto managerRequest1 = MemberRequestDto.ofManager(
                "tkv99@naver.com", "김김김", "rlaehdus00!!", "0105555555",
                "삼성", "SDI", "사장", "MANAGER"
        );
        MemberRequestDto managerRequest2 = MemberRequestDto.ofManager(
                "tkv9911@naver.com", "김도김도김도", "rlaehdus00!!", "0105552555",
                "삼성", "SDI", "부장", "MANAGER"
        );
        Company company = Company.builder()
                .invitationCode(null)
                .companyDept(managerRequest1.getCompanyDept())
                .companyName(managerRequest1.getCompanyName())
                .build();
        companyRepository.save(company);

        Member member1 = Member.createManager(managerRequest1, company);
        Member member2 = Member.createManager(managerRequest2, company);
        memberRepository.saveAll(List.of(member1, member2));

        Long member1_Id = memberRepository.findByEmail(member1.getEmail()).get().getMemberId();
        Long member2_Id = memberRepository.findByEmail(member2.getEmail()).get().getMemberId();

        //when
        MemberInfoResponseDto memberInfoResponseDto1 = memberRepository.getMemberInfo(member1_Id);
        MemberInfoResponseDto memberInfoResponseDto2 = memberRepository.getMemberInfo(member2_Id);

        //then
        assertThat(memberInfoResponseDto1).extracting(
                "name", "email", "companyName", "companyDept", "companyPosition"
        ).containsExactly(
                member1.getUserName(), member1.getEmail(), company.getCompanyName(), company.getCompanyDept(), member1.getCompanyPosition()
        );
        assertThat(memberInfoResponseDto2).extracting(
                "name", "email", "companyName", "companyDept", "companyPosition"
        ).containsExactly(
                member2.getUserName(), member2.getEmail(), company.getCompanyName(), company.getCompanyDept(), member2.getCompanyPosition()
        );
    }

    @Test
    @DisplayName("일반 회원의 정보와 회원의 회사 정보를 가지고 옵니다.")
    void getMemberInfoOfGeneral() {
        //given
        MemberRequestDto managerRequest1 = MemberRequestDto.ofGeneral(
                "tkv99@naver.com", "김김김", "rlaehdus00!!", "0105555555", "GENERAL"
        );
        MemberRequestDto managerRequest2 = MemberRequestDto.ofGeneral(
                "tkv9911@naver.com", "김도김도김도", "rlaehdus00!!", "0105552555", "GENERAL"
        );

        Member member1 = Member.createGeneral(managerRequest1);
        Member member2 = Member.createGeneral(managerRequest2);
        memberRepository.saveAll(List.of(member1, member2));

        Long member1_Id = memberRepository.findByEmail(member1.getEmail()).get().getMemberId();
        Long member2_Id = memberRepository.findByEmail(member2.getEmail()).get().getMemberId();

        //when
        MemberInfoResponseDto memberInfoResponseDto1 = memberRepository.getMemberInfo(member1_Id);
        MemberInfoResponseDto memberInfoResponseDto2 = memberRepository.getMemberInfo(member2_Id);

        //then
        assertThat(memberInfoResponseDto1).extracting(
                "name", "email", "companyName", "companyDept", "companyPosition"
        ).containsExactly(
                member1.getUserName(), member1.getEmail(), null, null, member1.getCompanyPosition()
        );
        assertThat(memberInfoResponseDto2).extracting(
                "name", "email", "companyName", "companyDept", "companyPosition"
        ).containsExactly(
                member2.getUserName(), member2.getEmail(), null, null, member2.getCompanyPosition()
        );

    }

    @Test
    @DisplayName("[성공케이스]-이름과 전화번호로 멤버를 찾습니다.")
    void findMemberByMemberNameAndPhoneNumber(){
        //given
        initMemberOfGeneral();
        String memberName="김2";
        String phoneNumber="01012345678";

        //when
        Member findMember=memberRepository.findMemberByNameAndPhoneNumber(memberName,phoneNumber).orElse(null);

        //then
        assertThat(findMember).isNotNull();
        assertThat(findMember.getMemberStatus()).isEqualTo(MemberStatus.GENERAL);
        assertThat(findMember.getPassword()).isEqualTo("rlaehdus22!!");
        assertThat(findMember.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(findMember.getUserName()).isEqualTo(memberName);
    }

    private void initMemberOfGeneral() {
        MemberRequestDto requestDto1=MemberRequestDto.ofGeneral("tkv11@naver.com","김1","rlaehdus11!!","01012345678","GENERAL");
        MemberRequestDto requestDto2=MemberRequestDto.ofGeneral("tkv22@naver.com","김2","rlaehdus22!!","01012345678","GENERAL");
        MemberRequestDto requestDto3=MemberRequestDto.ofGeneral("tkv33@naver.com","김3","rlaehdus33!!","01012345678","GENERAL");
        MemberRequestDto requestDto4=MemberRequestDto.ofGeneral("tkv44@naver.com","김4","rlaehdus44!!","01012345678","GENERAL");
        MemberRequestDto requestDto5=MemberRequestDto.ofGeneral("tkv55@naver.com","김5","rlaehdus55!!","01012345678","GENERAL");
        MemberRequestDto requestDto6=MemberRequestDto.ofGeneral("tkv66@naver.com","김6","rlaehdus66!!","01012345672","GENERAL");

        Member member1=Member.createGeneral(requestDto1);
        Member member2=Member.createGeneral(requestDto2);
        Member member3=Member.createGeneral(requestDto3);
        Member member4=Member.createGeneral(requestDto4);
        Member member5=Member.createGeneral(requestDto5);
        Member member6=Member.createGeneral(requestDto6);

        memberRepository.saveAll(List.of(member1,member2,member3,member4,member5,member6));
    }
}
