package example.demo.domain.member.repository;

import example.demo.domain.company.Company;

import example.demo.domain.company.dto.response.CompanyResponseDto;
import example.demo.domain.company.repository.CompanyRepository;
import example.demo.domain.member.Member;
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
    private EntityManager em;

    @AfterEach
    void teardown() {
        memberRepository.deleteAllInBatch();
    }

    private void initMemberOfGeneral() {
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

    private record Result(Company company1, Company company2,
                          Member member1, Member member2,
                          Member member3, Member member4,
                          Member member5,Member member6,
                          Member member7) {
    }

    @NotNull
    private Result getResult() {
        String COMPANY_CODE1="TEST1";
        String COMPANY_CODE2="TEST2";
        Company company1 = Company
                .builder()
                .companyDept("AI1")
                .companyName("SK1")
                .invitationCode(COMPANY_CODE1)
                .build();
        Company company2 = Company
                .builder()
                .companyDept("AI2")
                .companyName("SK2")
                .invitationCode(COMPANY_CODE2)
                .build();
        companyRepository.saveAll(List.of(company1, company2));

        MemberRequestDto memberRequestDto1 = MemberRequestDto.ofManager(
                "tkv123@naver.com", "김도연1", "abcd123!", "01012345678", "SK1", "AI1", "부사장", "MANAGER"
        );
        MemberRequestDto memberRequestDto2 = MemberRequestDto.ofManager(
                "tkv124@naver.com", "김도연2", "abcd123!12", "01012345679", "SK1", "AI1", "부사장", "MANAGER"
        );
        MemberRequestDto memberRequestDto3 = MemberRequestDto.ofManager(
                "tkv124@naver.com", "김도연3", "abcd123!13", "01012345679", "SK2", "AI2", "사장3", "MANAGER"
        );
        MemberRequestDto memberRequestDto4 = MemberRequestDto.ofManager(
                "tkv124@naver.com", "김도연4", "abcd123!14", "01012345679", "SK1", "AI1", "부사장", "MANAGER"
        );

        MemberRequestDto memberRequestDto5 = MemberRequestDto.ofManager(
                "tkv125@naver.com", "김도연5", "abcd123!2", "01012345670", "SK1", "AI1", "부사장", "MANAGER"
        );
        MemberRequestDto memberRequestDto6 = MemberRequestDto.ofManager(
                "tkv125@naver.com", "안현석1", "abcd123!2", "01012345672", "SK1", "AI1", "부사장", "MANAGER"
        );
        MemberRequestDto memberRequestDto7 = MemberRequestDto.ofManager(
                "tkv125@naver.com", "안현석2", "abcd123!2", "01012345672", "SK1", "AI1", "사장3", "MANAGER"
        );

        Member member1 = Member.createManager(memberRequestDto1, company1);
        Member member2 = Member.createManager(memberRequestDto2, company1);
        Member member3 = Member.createManager(memberRequestDto3, company2);
        Member member4 = Member.createManager(memberRequestDto4, company1);
        Member member5 = Member.createManager(memberRequestDto5, company1);
        Member member6 = Member.createManager(memberRequestDto6, company1);
        Member member7 = Member.createManager(memberRequestDto7, company1);
        member1 = memberRepository.save(member1);
        member2 = memberRepository.save(member2);
        member3 = memberRepository.save(member3);
        member4 = memberRepository.save(member4);
        member5 = memberRepository.save(member5);
        member6 = memberRepository.save(member6);
        member7 = memberRepository.save(member7);
        em.flush();
        em.clear();
        Result result = new Result(company1, company2, member1, member2, member3, member4, member5,member6,member7);
        return result;
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
        Long count = memberCustomRepository.getPhoneNumberCount("01050299737");
        Long NaNCount = memberCustomRepository.getPhoneNumberCount("01012345678");

        //then
        assertThat(count).isEqualTo(3L);
        assertThat(NaNCount).isEqualTo(0L);
    }

    @Test
    @DisplayName("회사id로 해당 회사 정보를 불러옵니다.")
    void getCompanyInfo() {
        //given
        Result result = getResult();

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
        Result result = getResult();

        //페이징 케이스
        Pageable page1 = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "name"));
        Pageable page2 = PageRequest.of(1, 3, Sort.by(Sort.Direction.DESC, "name"));

        //when
        Page<CompanyEmployeeResponseDto> result1 = memberRepository.getCompanyEmployeeInfo(result.company1.getCompanyId(), page1);
        Page<CompanyEmployeeResponseDto> result2 = memberRepository.getCompanyEmployeeInfo(result.company1.getCompanyId(), page2);

        //then
        assertThat(result1.getTotalPages()).isEqualTo(2);
        assertThat(result1.getTotalElements()).isEqualTo(6);
        assertThat(result1.getContent().size()).isEqualTo(3);
        assertThat(result2.getContent().size()).isEqualTo(3);
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
    @DisplayName("회사직원 이름을 검색하여 페이징 조회합니다.")
    void searchCompanyEmployeeInfo_name() {
        //given
        Result result = getResult();

        //페이징 케이스
        Pageable page1 = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "name"));
        Pageable page2 = PageRequest.of(1, 3, Sort.by(Sort.Direction.DESC, "name"));

        //when
        Page<CompanyEmployeeResponseDto> result1 = memberRepository.searchCompanyEmployeeInfo(result.company1.getCompanyId(),"안현", page1);
        Page<CompanyEmployeeResponseDto> result2 = memberRepository.searchCompanyEmployeeInfo(result.company1.getCompanyId(),"김도", page2);

        //then
        assertThat(result1.getTotalPages()).isEqualTo(1);
        assertThat(result1.getTotalElements()).isEqualTo(2);
        assertThat(result1.getContent().size()).isEqualTo(2);
        assertThat(result2.getContent().size()).isEqualTo(1);
    }

}
