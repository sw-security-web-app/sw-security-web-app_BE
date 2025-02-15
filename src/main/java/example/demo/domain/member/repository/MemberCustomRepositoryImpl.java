package example.demo.domain.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import example.demo.domain.company.QCompany;
import example.demo.domain.company.dto.QCompanyInfoWithUuidDto;
import example.demo.domain.member.dto.response.MemberInfoResponseDto;
import example.demo.domain.member.dto.response.QMemberInfoResponseDto;
import example.demo.domain.member.repository.MemberCustomRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static example.demo.domain.company.QCompany.*;
import static example.demo.domain.member.QMember.*;
@Repository("memberCustomRepositoryImpl")
public class MemberCustomRepositoryImpl implements MemberCustomRepository {
    private final JPAQueryFactory queryFactory;
    public MemberCustomRepositoryImpl(EntityManager em){
        this.queryFactory=new JPAQueryFactory(em);
    }

    @Override
    public Long getSameEmailCount(String email) {
         return Optional.ofNullable(
                queryFactory
                        .select(member.count())
                        .from(member)
                        .where(member.email.eq(email))
                        .fetchOne()
        ).orElse(0L);
    }

    @Override
    public Long getPhoneNumberCount(String phoneNumber) {
        return Optional.ofNullable(
                queryFactory
                        .select(member.count())
                        .from(member)
                        .where(member.phoneNumber.eq(phoneNumber))
                        .fetchOne()
        ).orElse(0L);
    }

    @Override
    public MemberInfoResponseDto getMemberInfo(Long memberId) {
        return queryFactory
                .select(new QMemberInfoResponseDto(
                        member.userName,
                        member.email,
                        member.company.companyName,
                        member.company.companyDept,
                        member.companyPosition
                ))
                .from(member)
                .leftJoin(member.company,company)
                .where(member.memberId.eq(memberId))
                .fetchOne();
    }
}
