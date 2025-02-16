package example.demo.util;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import example.demo.domain.member.QMember;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;


public class QueryDslUtil {
    private static String[] companyDept={
            "사장", "부사장", "전무", "상무", "이사", "부장", "차장", "과장", "대리", "주임", "사원", "인턴", "기타"
    };
    public static OrderSpecifier<?> getSortedColumn(Order order, Path<?> parent, String fieldName){
        Path<Object> fieldPath= Expressions.path(Object.class,parent,fieldName);
        return new OrderSpecifier(order,fieldPath);
    }

    //멤버의 이름으로 가나다순 정렬
    public static List<OrderSpecifier<?>> getOrderSpecifiers(Pageable pageable){
        List<OrderSpecifier<?>> orders=new ArrayList<>();

        if(!pageable.getSort().isEmpty()){
            for (Sort.Order order:pageable.getSort()){
                String property=order.getProperty();
                Order direction=order.getDirection().isAscending() ? Order.ASC : Order.DESC;

                if(property.equals("position")){
                    orders.add(getSortedByCompanyDept(direction));
                }else if(property.equals("name")){
                    orders.add(new OrderSpecifier<>(direction,QMember.member.userName));
                }
            }
        }

        //정렬 기준없으면 이름 오름차순
        if(orders.isEmpty()){
            orders.add(QMember.member.userName.asc());
        }
        return orders;
    }

    //직급 순서대로 정렬
    private static OrderSpecifier<Integer>getSortedByCompanyDept(Order order){
        NumberExpression<Integer> rank=new CaseBuilder()
                .when(QMember.member.companyPosition.eq(companyDept[0])).then(0)
                .when(QMember.member.companyPosition.eq(companyDept[1])).then(1)
                .when(QMember.member.companyPosition.eq(companyDept[2])).then(2)
                .when(QMember.member.companyPosition.eq(companyDept[3])).then(3)
                .when(QMember.member.companyPosition.eq(companyDept[4])).then(4)
                .when(QMember.member.companyPosition.eq(companyDept[5])).then(5)
                .when(QMember.member.companyPosition.eq(companyDept[6])).then(6)
                .when(QMember.member.companyPosition.eq(companyDept[7])).then(7)
                .when(QMember.member.companyPosition.eq(companyDept[8])).then(8)
                .when(QMember.member.companyPosition.eq(companyDept[9])).then(9)
                .when(QMember.member.companyPosition.eq(companyDept[10])).then(10)
                .when(QMember.member.companyPosition.eq(companyDept[11])).then(11)
                .when(QMember.member.companyPosition.eq(companyDept[12])).then(12)
                //나머지 후순위 배치
                .otherwise(99);
        return new OrderSpecifier<>(order,rank);
    }
}
