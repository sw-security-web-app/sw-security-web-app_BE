package example.demo.domain.company.api;

import example.demo.domain.company.CompanyRepository;
import example.demo.domain.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class CompanyServiceTest {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private MemberRepository memberRepository;

}