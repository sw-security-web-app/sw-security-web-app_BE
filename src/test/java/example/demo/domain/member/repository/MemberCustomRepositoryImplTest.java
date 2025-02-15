package example.demo.domain.member.repository;

import example.demo.domain.company.Company;
import example.demo.domain.company.repository.CompanyRepository;
import example.demo.domain.member.Member;
import example.demo.domain.member.dto.request.MemberRequestDto;
import example.demo.domain.member.dto.response.CompanyEmployeeResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
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
    @AfterEach
    void teardown(){
        memberRepository.deleteAllInBatch();
    }
    @Test
    @DisplayName("같은 이메일의 개수를 반환하며 존재하지 않는다면 0L을 반환합니다.")
    void getSameEmailCount() {
        //given
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

        //when
        Long count=memberCustomRepository.getPhoneNumberCount("01050299737");
        Long NaNCount=memberCustomRepository.getPhoneNumberCount("01012345678");

        //then
        assertThat(count).isEqualTo(3L);
        assertThat(NaNCount).isEqualTo(0L);
    }

    @Test
    @DisplayName("회사id로 해당 회사소속의 직원목록을 불러옵니다.")
    void getCompanyEmployeeInfo(){
        //given
        String COMPANY_CODE="TEST_CODE";
        MemberRequestDto managerDto=MemberRequestDto.ofManager(
                "tkv00@naver.com", "김김0", "rlaehdus00!!", "01050299737","SK","AI","사장","MANAGER"
        );
        MemberRequestDto employee1Dto=MemberRequestDto.ofEmployee(
                "tkv11@naver.com", "김김1", "rlaehdus00!!", "01050299737","인턴1",COMPANY_CODE,"EMPLOYEE"
        );
        MemberRequestDto employee2Dto=MemberRequestDto.ofEmployee(
                "tkv22@naver.com", "김김2", "rlaehdus00!!", "01050299737","인턴2",COMPANY_CODE,"EMPLOYEE"
        );

        Company company=Company
                .builder()
                .companyDept("AI")
                .companyName("SK")
                .invitationCode(COMPANY_CODE)
                .build();

        company=companyRepository.save(company);

        Member manager=Member.createManager(managerDto,company);
        Member employee1=Member.createEmployee(employee1Dto,company);
        Member employee2=Member.createEmployee(employee2Dto,company);
        memberRepository.saveAll(List.of(manager,employee1,employee2));

        //when
        List<CompanyEmployeeResponseDto> employeeList=memberCustomRepository.getCompanyEmployeeInfo(company.getCompanyId());

        //then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).hasSize(3);

        //직원 검증
        assertThat(employeeList)
                .extracting(CompanyEmployeeResponseDto::getName)
                .containsExactlyInAnyOrder("김김0","김김1","김김2");

        assertThat(employeeList)
                .extracting(CompanyEmployeeResponseDto::getEmail)
                .containsExactlyInAnyOrder("tkv00@naver.com","tkv11@naver.com","tkv22@naver.com");

        assertThat(employeeList)
                .extracting(CompanyEmployeeResponseDto::getCompanyPosition)
                .containsExactlyInAnyOrder("사장","인턴1","인턴2");
    }
}