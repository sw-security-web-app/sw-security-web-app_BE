package example.demo.domain.member;

import example.demo.domain.company.Company;
import example.demo.domain.member.dto.request.MemberRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "member_email")
    private String email;

    @Column(name = "member_name")
    private String userName;

    @Column(name = "member_password")
    private String password;

    @Column(name = "member_status")
    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    @Column(name = "member_phone_number")
    private String phoneNumber;

    //Company랑 양방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    public Member(String email, String userName, String password, MemberStatus memberStatus, String phoneNumber, Company company) {
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.memberStatus = memberStatus;
        this.phoneNumber = phoneNumber;
        this.company = company;
    }

    //일반 유저
    public static Member createGeneral(MemberRequestDto memberRequestDto){
        return new Member(memberRequestDto.getEmail(),memberRequestDto.getName(),memberRequestDto.getPassword(),
                    MemberStatus.GENERAL,memberRequestDto.getPhoneNumber(),null
                );
    }
    //관리자
    public static Member createManager(MemberRequestDto memberRequestDto,Company company){
        return new Member(memberRequestDto.getEmail(),memberRequestDto.getName(),memberRequestDto.getPassword(),
                MemberStatus.MANAGER,memberRequestDto.getPhoneNumber(),company
        );
    }
    //직원
    public static Member createEmployee(MemberRequestDto memberRequestDto,Company company) {
        return new Member(memberRequestDto.getEmail(), memberRequestDto.getName(), memberRequestDto.getPassword(),
                MemberStatus.EMPLOYEE, memberRequestDto.getPhoneNumber(), company
        );
    }
}
