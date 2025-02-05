package example.demo.domain.company.api;

import example.demo.domain.company.repository.CompanyRepository;
import example.demo.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CompanyServiceTest {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private MemberRepository memberRepository;

}