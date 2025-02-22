package example.demo.domain.member.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * example.demo.domain.member.dto.response.QMemberInfoResponseDto is a Querydsl Projection type for MemberInfoResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMemberInfoResponseDto extends ConstructorExpression<MemberInfoResponseDto> {

    private static final long serialVersionUID = 1144234121L;

    public QMemberInfoResponseDto(com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<String> email, com.querydsl.core.types.Expression<String> companyName, com.querydsl.core.types.Expression<String> companyDept, com.querydsl.core.types.Expression<String> companyPosition) {
        super(MemberInfoResponseDto.class, new Class<?>[]{String.class, String.class, String.class, String.class, String.class}, name, email, companyName, companyDept, companyPosition);
    }

}

