package example.demo.util;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import example.demo.domain.member.QMember;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;


public class QueryDslUtil {
    public static OrderSpecifier<?> getSortedColumn(Order order, Path<?> parent, String fieldName){
        Path<Object> fieldPath= Expressions.path(Object.class,parent,fieldName);
        return new OrderSpecifier(order,fieldPath);
    }

    //멤버의 이름으로 가나다순 정렬
    public static List<OrderSpecifier> getAllOrderByMemberNameSpecifies(Pageable pageable){
        List<OrderSpecifier> orders=new ArrayList<>();

        if(!pageable.getSort().isEmpty()){
            for (Sort.Order order:pageable.getSort()){
                Order direction=order.getDirection().isAscending()?Order.ASC:Order.DESC;
                OrderSpecifier<?> orderSpecifier=new OrderSpecifier<>(direction, QMember.member.userName);
                orders.add(orderSpecifier);
            }
        }else {
            orders.add(QMember.member.userName.asc());
        }
        return orders;
    }
}
