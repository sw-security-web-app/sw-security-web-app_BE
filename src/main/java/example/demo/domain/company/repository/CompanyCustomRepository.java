package example.demo.domain.company.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import example.demo.domain.company.dto.CompanyCodeDto;
import example.demo.domain.company.dto.CompanyInfoWithUuidDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface CompanyCustomRepository {
    //uuid로 직원 유저의 회사명, 부서명 가져오기
    CompanyInfoWithUuidDto findCompanyInfoByInvitationCode(String inviteCode);
    //유저가 관리자인지 확인하고 해당 유저의 회사 초대코드를 불러옵니다.
  CompanyCodeDto findCompanyCode(Long memberId);

}
