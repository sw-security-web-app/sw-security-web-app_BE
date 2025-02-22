package example.demo.domain.company.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * example.demo.domain.company.dto.response.QCompanyResponseDto is a Querydsl Projection type for CompanyResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QCompanyResponseDto extends ConstructorExpression<CompanyResponseDto> {

    private static final long serialVersionUID = -920559413L;

    public QCompanyResponseDto(com.querydsl.core.types.Expression<String> companyName, com.querydsl.core.types.Expression<String> companyDept) {
        super(CompanyResponseDto.class, new Class<?>[]{String.class, String.class}, companyName, companyDept);
    }

}

