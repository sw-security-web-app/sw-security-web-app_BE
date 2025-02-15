package example.demo.domain.member.repository;

import example.demo.domain.company.Company;
import example.demo.domain.company.repository.CompanyRepository;
import example.demo.domain.member.Member;
import example.demo.domain.member.dto.request.MemberRequestDto;
import example.demo.domain.member.dto.response.MemberInfoResponseDto;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class MemberCustomRepositoryImplTest {
    @Autowired
    @Qualifier("memberCustomRepositoryImpl")
    private MemberCustomRepository memberCustomRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CompanyRepository companyRepository;
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
    @DisplayName("관리자 회원의 정보와 회원의 회사 정보를 가지고 옵니다.")
    void getMemberInfo(){
        //given
        MemberRequestDto managerRequest1=MemberRequestDto.ofManager(
                "tkv99@naver.com", "김김김", "rlaehdus00!!","0105555555",
                "삼성","SDI","사장","MANAGER"
        );
        MemberRequestDto managerRequest2=MemberRequestDto.ofManager(
                "tkv9911@naver.com", "김도김도김도", "rlaehdus00!!","0105552555",
                "삼성","SDI","부장","MANAGER"
        );
        Company company=Company.builder()
                .invitationCode(null)
                .companyDept(managerRequest1.getCompanyDept())
                .companyName(managerRequest1.getCompanyName())
                .build();
        companyRepository.save(company);

        Member member1=Member.createManager(managerRequest1,company);
        Member member2=Member.createManager(managerRequest2,company);
        memberRepository.saveAll(List.of(member1,member2));

        Long member1_Id=memberRepository.findByEmail(member1.getEmail()).get().getMemberId();
        Long member2_Id=memberRepository.findByEmail(member2.getEmail()).get().getMemberId();

        //when
        MemberInfoResponseDto memberInfoResponseDto1=memberRepository.getMemberInfo(member1_Id);
        MemberInfoResponseDto memberInfoResponseDto2=memberRepository.getMemberInfo(member2_Id);

        //then
        assertThat(memberInfoResponseDto1).extracting(
                "name","email","companyName","companyDept","companyPosition"
        ).containsExactly(
                member1.getUserName(),member1.getEmail(),company.getCompanyName(),company.getCompanyDept(),member1.getCompanyPosition()
        );
        assertThat(memberInfoResponseDto2).extracting(
                "name","email","companyName","companyDept","companyPosition"
        ).containsExactly(
                member2.getUserName(),member2.getEmail(),company.getCompanyName(),company.getCompanyDept(),member2.getCompanyPosition()
        );
    }
}