package example.demo.util;

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

import static example.demo.domain.member.QMember.*;
import static example.demo.util.CompanyPosition.*;


public class QueryDslUtil {

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
                    orders.add(new OrderSpecifier<>(direction, member.userName));
                }
            }
        }

        //정렬 기준없으면 이름 오름차순
        if(orders.isEmpty()){
            orders.add(member.userName.asc());
        }
        return orders;
    }

    //직급 순서대로 정렬
    private static OrderSpecifier<Integer>getSortedByCompanyDept(Order order){
        NumberExpression<Integer> rank=new CaseBuilder()
                .when(member.companyPosition.eq(CEO.getPosition())).then(0)
                .when(member.companyPosition.eq(VICE_PRESIDENT.getPosition())).then(1)
                .when(member.companyPosition.eq(EXECUTIVE_DIRECTOR.getPosition())).then(2)
                .when(member.companyPosition.eq(MANAGING_DIRECTOR.getPosition())).then(3)
                .when(member.companyPosition.eq(DIRECTOR.getPosition())).then(4)
                .when(member.companyPosition.eq(GENERAL_MANAGER.getPosition())).then(5)
                .when(member.companyPosition.eq(DEPUTY_GENERAL_MANAGER.getPosition())).then(6)
                .when(member.companyPosition.eq(MANAGER.getPosition())).then(7)
                .when(member.companyPosition.eq(ASSISTANT_MANAGER.getPosition())).then(8)
                .when(member.companyPosition.eq(SENIOR_STAFF.getPosition())).then(9)
                .when(member.companyPosition.eq(STAFF.getPosition())).then(10)
                .when(member.companyPosition.eq(INTERN.getPosition())).then(11)
                .when(member.companyPosition.eq(OTHER.getPosition())).then(12)
                //나머지 후순위 배치
                .otherwise(99);
        return new OrderSpecifier<>(order,rank);
    }
}
