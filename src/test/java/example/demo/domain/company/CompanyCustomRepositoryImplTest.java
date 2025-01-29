package example.demo.domain.company;

import example.demo.domain.company.dto.CompanyCodeDto;
import example.demo.domain.company.dto.CompanyInfoWithUuidDto;
import example.demo.domain.member.Member;
import example.demo.domain.member.MemberErrorCode;
import example.demo.domain.member.MemberRepository;
import example.demo.domain.member.api.MemberService;
import example.demo.domain.member.dto.request.MemberRequestDto;
import example.demo.error.RestApiException;
import example.demo.util.CreateUuid;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CompanyCustomRepositoryImplTest {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private MemberRepository memberRepository;


    @Test
    @DisplayName("초대코드로 회사이름과 회사부서명을 조회합니다.")
    void findCompanyInfoByInvitationCode() {
        //given
        String uuid= CreateUuid.createShortUuid();
        Company company=new Company("company1","개발","사장",uuid);

        //when
        companyRepository.save(company);
        CompanyInfoWithUuidDto companyInfoWithUuidDto
                =companyRepository.findCompanyInfoByInvitationCode(uuid);
        //then
        assertThat(companyInfoWithUuidDto).extracting("companyName","companyDept")
                .containsExactlyInAnyOrder("company1","개발");

    }
    @Test
    @DisplayName("초대코드로 회사를 찾을 수 없을 때 에러를 반환합니다.")
    void findNotCompanyInfoByInvitationCode(){
        //given
        String uuid= CreateUuid.createShortUuid();
        Company company=new Company("company1","개발","사장",uuid);
        String incorrectUuid="00000000";
        //when
        companyRepository.save(company);

        //then
        assertThatThrownBy(()->companyRepository.findCompanyInfoByInvitationCode(incorrectUuid))
                .isInstanceOf(RestApiException.class)
                .hasMessage("회사가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("해당 유저가 관리자인지 확인하고 회사 초대코드를 반환합니다.")
    void returnCompanyCode() {
       //given
        String uuid=CreateUuid.createShortUuid();
        MemberRequestDto memberRequestDto=MemberRequestDto.ofManager("tkv00@naver.com","김도연","rlaehdus00!!!","01012345678","삼성","개발","사장");
        Company company=Company.builder()
                .companyDept(memberRequestDto.getCompanyDept())
                .companyPosition(memberRequestDto.getCompanyPosition())
                .companyName(memberRequestDto.getCompanyName())
                .invitationCode(uuid)
                .build();
        companyRepository.save(company);
        Member member=Member.createManager(memberRequestDto,company);
        memberRepository.save(member);

        Long findMemberId=memberRepository.findByEmailAndPhoneNumber(memberRequestDto.getEmail(),memberRequestDto.getPhoneNumber())
                .orElseThrow(()-> new RestApiException(MemberErrorCode.INVALID_MEMBER_STATUS)).getMemberId();
        //when
        CompanyCodeDto codeDto=companyRepository.findCompanyCode(findMemberId);

       //then
        assertThat(codeDto.getCompanyCode()).isEqualTo(uuid);

    }
}