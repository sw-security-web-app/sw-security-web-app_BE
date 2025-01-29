package example.demo.domain.company;

import example.demo.domain.company.dto.CompanyInfoWithUuidDto;
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
class CompanyCustomRepositoryImplTest {
    @Autowired
    private CompanyRepository companyRepository;
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
}