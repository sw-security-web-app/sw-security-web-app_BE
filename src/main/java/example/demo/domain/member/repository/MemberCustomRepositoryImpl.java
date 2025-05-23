package example.demo.domain.member.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import example.demo.domain.member.Member;
import example.demo.domain.member.MemberStatus;
import example.demo.domain.member.QMember;
import example.demo.domain.member.dto.response.CompanyEmployeeResponseDto;
import example.demo.domain.member.dto.response.QCompanyEmployeeResponseDto;
import example.demo.util.QueryDslUtil;
import example.demo.domain.company.QCompany;
import example.demo.domain.company.dto.QCompanyInfoWithUuidDto;
import example.demo.domain.member.dto.response.MemberInfoResponseDto;
import example.demo.domain.member.dto.response.QMemberInfoResponseDto;
import example.demo.domain.member.repository.MemberCustomRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static example.demo.domain.company.QCompany.*;
import static example.demo.domain.member.QMember.*;

@Repository("memberCustomRepositoryImpl")
public class MemberCustomRepositoryImpl implements MemberCustomRepository {
    private final JPAQueryFactory queryFactory;

    public MemberCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
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
    public Page<CompanyEmployeeResponseDto> getCompanyEmployeeInfo(Long companyId, Pageable pageable, String search) {
        //정렬 추가
        List<OrderSpecifier<?>> orderSpecifiers = QueryDslUtil.getOrderSpecifiers(pageable);

        //정렬 조건

        List<CompanyEmployeeResponseDto> content = queryFactory
                .select(new QCompanyEmployeeResponseDto(
                        member.companyPosition,
                        member.userName,
                        member.email,
                        member.memberId
                ))
                .from(member)
                .leftJoin(member.company, company)
                .where(allCompanyIdEq(company.companyId, companyId,search))
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(member.count())
                .from(member)
                .leftJoin(member.company, company)
                .where(allCompanyIdEq(company.companyId, companyId,search));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    public MemberInfoResponseDto getMemberInfo(Long memberId) {
        return queryFactory
                .select(new QMemberInfoResponseDto(
                        member.userName,
                        member.email,
                        member.company.companyName,
                        member.company.companyDept,
                        member.companyPosition,
                        member.memberStatus
                ))
                .from(member)
                .leftJoin(member.company, company)
                .where(member.memberId.eq(memberId))
                .fetchOne();

    }

    @Override
    public Optional<Member> findMemberByNameAndPhoneNumber(String name, String phoneNumber) {
        return Optional.ofNullable(queryFactory
                .selectFrom(member)
                .where(phoneNumberEq(phoneNumber).and(memberNameEq(name)))
                .fetchOne());
    }

    private BooleanExpression companyIdEq(Long companyId) {
        return companyId == null ? null : company.companyId.eq(companyId);
    }

    private BooleanExpression companyIdOfMemberEq(NumberPath<Long> companyId) {
        return companyId == null ? null : member.company.companyId.eq(companyId);
    }

    private BooleanExpression allCompanyIdEq(NumberPath<Long> companyOfCompanyId, Long inputCompanyId, String memberName) {
        BooleanExpression companyCondition = companyIdEq(inputCompanyId);
        BooleanExpression memberCondition = companyIdOfMemberEq(companyOfCompanyId);
        BooleanExpression memberNameCondition = memberNameLike(memberName);


        if (companyCondition == null) return memberCondition;
        if (memberCondition == null) return companyCondition;
        if(memberNameCondition == null) return companyCondition.and(memberCondition);

        //이름값이 있는 경우 이름도 같이 검색 조건 추가
        return companyCondition.and(memberCondition).and(memberNameCondition);
    }

    private BooleanExpression phoneNumberEq(String phoneNumber) {
        return phoneNumber == null ? null : member.phoneNumber.eq(phoneNumber);
    }

    private BooleanExpression memberNameEq(String name) {
        return name == null ? null : member.userName.eq(name);
    }

    private BooleanExpression memberNameLike(String name){
        return name==null ? null : member.userName.contains(name);
    }



}
