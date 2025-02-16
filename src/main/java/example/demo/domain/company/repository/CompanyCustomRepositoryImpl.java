package example.demo.domain.company.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import example.demo.domain.company.Company;
import example.demo.domain.company.CompanyErrorCode;
import example.demo.domain.company.QCompany;
import example.demo.domain.company.dto.CompanyCodeDto;
import example.demo.domain.company.dto.CompanyInfoWithUuidDto;
import example.demo.domain.company.dto.QCompanyCodeDto;
import example.demo.domain.company.dto.QCompanyInfoWithUuidDto;
import example.demo.domain.company.dto.response.CompanyResponseDto;
import example.demo.domain.company.dto.response.QCompanyResponseDto;
import example.demo.error.RestApiException;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static example.demo.domain.company.QCompany.*;
import static example.demo.domain.member.QMember.*;

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

    @Override
    public CompanyCodeDto findCompanyCode(Long memberId) {
        return Optional.ofNullable(
                queryFactory
                        .select(new QCompanyCodeDto(company.invitationCode))
                        .from(member)
                        .join(company)
                        .on(member.company.companyId.eq(company.companyId))
                        .where(member.memberId.eq(memberId))
                        .fetchOne()
        ).orElseThrow(()->new RestApiException(CompanyErrorCode.NOT_EXIST_COMPANY));
    }

    @Override
    public CompanyResponseDto getCompanyInfo(Long memberId) {
        return queryFactory
                .select(new QCompanyResponseDto(
                        company.companyName,
                        company.companyDept
                ))
                .from(member)
                .join(member.company,company)
                .where(member.memberId.eq(memberId))
                .fetchOne();
    }
}
