package example.demo.domain.member.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * example.demo.domain.member.dto.response.QCompanyEmployeeResponseDto is a Querydsl Projection type for CompanyEmployeeResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QCompanyEmployeeResponseDto extends ConstructorExpression<CompanyEmployeeResponseDto> {

    private static final long serialVersionUID = -722697152L;

    public QCompanyEmployeeResponseDto(com.querydsl.core.types.Expression<String> companyPosition, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<String> email) {
        super(CompanyEmployeeResponseDto.class, new Class<?>[]{String.class, String.class, String.class}, companyPosition, name, email);
    }

}

