package example.demo.domain.company;

import example.demo.domain.BaseEntity;
import example.demo.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "company_dept")
    private String companyDept;

    @Column(name = "company_position")
    private String companyPosition;

    //Member랑 양방향
    @OneToMany(mappedBy = "company",cascade = CascadeType.ALL)
    private List<Member> members=new ArrayList<>();
}

