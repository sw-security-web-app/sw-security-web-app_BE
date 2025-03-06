package example.demo.domain.member;

import example.demo.domain.BaseEntity;
import example.demo.domain.chat.ChatRoom;
import example.demo.domain.company.Company;
import example.demo.domain.member.dto.request.MemberRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseEntity {
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

    @Column(name = "company_position")
    private String companyPosition;

    //계정 잠금 여부
    @Column(name = "account_locked",columnDefinition = "BOOLEAN DEFAULT false")
    private boolean accountLocked;
    //Company랑 양방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;
    public Member(String email, String userName, String password, MemberStatus memberStatus, String phoneNumber, String companyPosition,Company company) {
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.memberStatus = memberStatus;
        this.phoneNumber = phoneNumber;
        this.company = company;
        this.companyPosition=companyPosition;
        this.accountLocked=isAccountLocked();
    }

    //일반 유저
    public static Member createGeneral(MemberRequestDto memberRequestDto){
        return new Member(memberRequestDto.getEmail(),memberRequestDto.getName(),memberRequestDto.getPassword(),
                    MemberStatus.GENERAL,memberRequestDto.getPhoneNumber(),memberRequestDto.getCompanyPosition(),null
                );
    }
    //관리자
    public static Member createManager(MemberRequestDto memberRequestDto,Company company){

        return new Member(memberRequestDto.getEmail(), memberRequestDto.getName(),memberRequestDto.getPassword(),
                MemberStatus.MANAGER, memberRequestDto.getPhoneNumber(), memberRequestDto.getCompanyPosition(),company
        );
    }
    //직원
    public static Member createEmployee(MemberRequestDto memberRequestDto,Company company) {

        return new Member(memberRequestDto.getEmail(), memberRequestDto.getName(), memberRequestDto.getPassword(),
                MemberStatus.EMPLOYEE, memberRequestDto.getPhoneNumber(),memberRequestDto.getCompanyPosition(), company
        );
    }
    public void setCompany(Company company){
        this.company=company;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setMemberId(Long memberId){this.memberId=memberId;}


    //멤버의 Lock여부 변경
    public void changeMemberLock(boolean type){
        this.accountLocked=type;
    }

}
