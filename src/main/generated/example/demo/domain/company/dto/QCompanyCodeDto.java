package example.demo.domain.company.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * example.demo.domain.company.dto.QCompanyCodeDto is a Querydsl Projection type for CompanyCodeDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QCompanyCodeDto extends ConstructorExpression<CompanyCodeDto> {

    private static final long serialVersionUID = -304900680L;

    public QCompanyCodeDto(com.querydsl.core.types.Expression<String> companyCode) {
        super(CompanyCodeDto.class, new Class<?>[]{String.class}, companyCode);
    }

}

