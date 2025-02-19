package example.demo.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import example.demo.domain.member.QMember;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class QueryDslUtilTest {

    @Test
    @DisplayName("멤버의 이름을 기준으로 가나다순으로 정렬합니다.")
    void getAllOrderByMemberNameSpecifies_ASC() {
        //given
        Pageable pageable= PageRequest.of(0,10, Sort.by(Sort.Direction.ASC,"name"));

        //when
        List<OrderSpecifier<?>> orderSpecifiers=QueryDslUtil.getOrderSpecifiers(pageable);

        //then
        assertThat(orderSpecifiers).isNotEmpty();
        assertThat(orderSpecifiers.get(0)).isEqualTo(QMember.member.userName.asc());
    }
    @Test
    @DisplayName("멤버의 이름을 기준으로 가나다 역순으로 정렬합니다.")
    void getAllOrderByMemberNameSpecifies_DESC() {
        //given
        Pageable pageable= PageRequest.of(0,10, Sort.by(Sort.Direction.DESC,"name"));

        //when
        List<OrderSpecifier<?>> orderSpecifiers=QueryDslUtil.getOrderSpecifiers(pageable);

        //then
        assertThat(orderSpecifiers).isNotEmpty();
        assertThat(orderSpecifiers.get(0)).isEqualTo(QMember.member.userName.desc());
    }

    @Test
    @DisplayName("정렬기준이 없으면 오르차순(기본정렬)로 정렬합니다.")
    void getAllOrderByMemberNameSpecifies_DEFAULT() {
        //given
        Pageable pageable= PageRequest.of(0,10);

        //when
        List<OrderSpecifier<?>> orderSpecifiers=QueryDslUtil.getOrderSpecifiers(pageable);

        //then
        assertThat(orderSpecifiers).isNotEmpty();
        assertThat(orderSpecifiers.get(0)).isEqualTo(QMember.member.userName.asc());
    }

    @Test
    @DisplayName("직무별 오름차순으로 정렬합니다.")
    void getAllOrderByMemberDeptPosition(){
        //given
        Pageable pageable=PageRequest.of(0,10,Sort.by("position"));
        String[] companyDept={
                "사장", "부사장", "전무", "상무", "이사", "부장", "차장", "과장", "대리", "주임", "사원", "인턴", "기타"
        };
        //when
        List<OrderSpecifier<?>> orderSpecifiers=QueryDslUtil.getOrderSpecifiers(pageable);

        //then
        assertThat(orderSpecifiers).isNotEmpty();
        OrderSpecifier<?> orderSpecifier = orderSpecifiers.get(0);
        String orderSpecifierString = orderSpecifier.toString();

        assertThat(orderSpecifierString).contains("case when");

        for (String dept : companyDept) {
            assertThat(orderSpecifierString).contains(dept);
        }

        // 정렬 방향이 오름차순인지 확인
        assertThat(orderSpecifier.getOrder()).isEqualTo(Order.ASC);
    }
}