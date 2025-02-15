package example.demo.util;

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
        List<OrderSpecifier> orderSpecifiers=QueryDslUtil.getAllOrderByMemberNameSpecifies(pageable);

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
        List<OrderSpecifier> orderSpecifiers=QueryDslUtil.getAllOrderByMemberNameSpecifies(pageable);

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
        List<OrderSpecifier> orderSpecifiers=QueryDslUtil.getAllOrderByMemberNameSpecifies(pageable);

        //then
        assertThat(orderSpecifiers).isNotEmpty();
        assertThat(orderSpecifiers.get(0)).isEqualTo(QMember.member.userName.asc());
    }
}