package example.demo.domain.company;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCompany is a Querydsl query type for Company
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCompany extends EntityPathBase<Company> {

    private static final long serialVersionUID = -1618994007L;

    public static final QCompany company = new QCompany("company");

    public final example.demo.domain.QBaseEntity _super = new example.demo.domain.QBaseEntity(this);

    public final StringPath companyDept = createString("companyDept");

    public final NumberPath<Long> companyId = createNumber("companyId", Long.class);

    public final StringPath companyName = createString("companyName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath invitationCode = createString("invitationCode");

    public final ListPath<example.demo.domain.member.Member, example.demo.domain.member.QMember> members = this.<example.demo.domain.member.Member, example.demo.domain.member.QMember>createList("members", example.demo.domain.member.Member.class, example.demo.domain.member.QMember.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QCompany(String variable) {
        super(Company.class, forVariable(variable));
    }

    public QCompany(Path<? extends Company> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCompany(PathMetadata metadata) {
        super(Company.class, metadata);
    }

}

