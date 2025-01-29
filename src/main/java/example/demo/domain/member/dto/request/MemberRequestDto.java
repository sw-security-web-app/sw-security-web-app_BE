package example.demo.domain.member.dto.request;

import example.demo.domain.member.MemberStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class  MemberRequestDto{
    @Pattern(regexp ="^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$\n")
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;

    @NotBlank(message = "유저 형태는 필수 입력 값입니다.")
    private MemberStatus memberStatus;

    @Pattern(regexp = "^(010\\d{8}|\\d{3}\\d{7})$\n")
    @NotBlank(message = "휴대폰 번호는 필수 입력 값입니다.")
    private String phoneNumber;

    private String companyName;
    private String companyDept;
    private String companyPosition;
    //초대 코드
    private String invitationCode;

    //일반인 사용자
    @Builder
    private MemberRequestDto(String email,String name,String password,String phoneNumber){
        this.invitationCode=null;
        this.companyPosition=null;
        this.companyDept=null;
        this.companyName=null;
        this.memberStatus=MemberStatus.GENERAL;
        this.password=password;
        this.email=email;
        this.name=name;
        this.phoneNumber=phoneNumber;
    }
    //회사 관리자 사용자
    @Builder
    private MemberRequestDto(String email,String name,String password,String phoneNumber,String companyName,String companyDept,String companyPosition){
        this.companyName=companyName;
        this.companyDept=companyDept;
        this.companyPosition=companyPosition;
        this.memberStatus=MemberStatus.MANAGER;
        this.invitationCode=null;
        this.password=password;
        this.email=email;
        this.name=name;
        this.phoneNumber=phoneNumber;
    }

    //회사 직원 사용자
    @Builder
    private MemberRequestDto(String email,String name,String password,String phoneNumber,String companyPosition,String invitationCode){
        this.email=email;
        this.name=name;
        //TODO : 비밀번호 암호화 해야함.
        this.password=password;
        this.memberStatus=MemberStatus.EMPLOYEE;
        this.phoneNumber=phoneNumber;
        this.companyName=null;
        this.companyDept=null;
        this.companyPosition=companyPosition;
        this.invitationCode=invitationCode;
    }

}
