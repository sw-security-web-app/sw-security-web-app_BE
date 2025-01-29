package example.demo.domain.company;

import com.querydsl.jpa.impl.JPAQueryFactory;
import example.demo.domain.company.dto.CompanyInfoWithUuidDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


public interface CompanyCustomRepository {
    //uuid로 직원 유저의 회사명, 부서명 가져오기
    CompanyInfoWithUuidDto findCompanyInfoByInvitationCode(String inviteCode);

}
