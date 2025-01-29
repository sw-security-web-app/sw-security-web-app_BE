package example.demo.domain.company;

import com.querydsl.jpa.impl.JPAQueryFactory;
import example.demo.domain.company.dto.CompanyInfoWithUuidDto;
import example.demo.domain.company.dto.QCompanyInfoWithUuidDto;
import example.demo.domain.member.MemberRepositoryCustom;
import example.demo.error.RestApiException;
import jakarta.persistence.EntityManager;

import java.util.Optional;

import static example.demo.domain.company.QCompany.*;

public class CompanyCustomRepositoryImpl implements CompanyCustomRepository {

    private final JPAQueryFactory queryFactory;
    public CompanyCustomRepositoryImpl(EntityManager em){
        this.queryFactory=new JPAQueryFactory(em);
    }

    @Override
    public CompanyInfoWithUuidDto findCompanyInfoByInvitationCode(String inviteCode) {
        return Optional.ofNullable(
                queryFactory
                        .select(new QCompanyInfoWithUuidDto(company.companyName, company.companyDept))
                        .from(company)
                        .where(company.invitationCode.eq(inviteCode))
                        .fetchOne()
        ).orElseThrow(()->new RestApiException(CompanyErrorCode.NOT_EXIST_COMPANY));
    }
}
