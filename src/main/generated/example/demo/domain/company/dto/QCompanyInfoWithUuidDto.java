package example.demo.domain.company.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * example.demo.domain.company.dto.QCompanyInfoWithUuidDto is a Querydsl Projection type for CompanyInfoWithUuidDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QCompanyInfoWithUuidDto extends ConstructorExpression<CompanyInfoWithUuidDto> {

    private static final long serialVersionUID = 948712950L;

    public QCompanyInfoWithUuidDto(com.querydsl.core.types.Expression<String> companyName, com.querydsl.core.types.Expression<String> companyDept) {
        super(CompanyInfoWithUuidDto.class, new Class<?>[]{String.class, String.class}, companyName, companyDept);
    }

}

