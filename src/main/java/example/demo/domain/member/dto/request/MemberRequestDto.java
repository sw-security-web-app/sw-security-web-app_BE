package example.demo.domain.member.dto.request;

import example.demo.domain.member.MemberStatus;
import example.demo.util.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class  MemberRequestDto{
    @NotBlank(message = "이메일은 필수 입력 값입니다.",
                groups = ValidationGroups.NotEmptyGroup.class)
    @Pattern(regexp ="^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
            message = "잘못된 이메일 입력입니다.",
            groups = ValidationGroups.PatternCheckGroup.class
    )
    private String email;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.",
            groups = ValidationGroups.NotEmptyGroup.class)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "잘못된 비밀번호 형식입니다.",
            groups = ValidationGroups.PatternCheckGroup.class)
    private String password;

    private String memberStatus;

    @NotBlank(message = "휴대폰 번호는 필수 입력 값입니다.",
            groups = ValidationGroups.NotEmptyGroup.class)
    @Pattern(regexp = "^(010\\d{8}|\\d{3}\\d{7})$",
            message = "잘못된 휴대폰 번호 형식입니다.",
            groups = ValidationGroups.PatternCheckGroup.class)
    private String phoneNumber;

    private String companyName;
    private String companyDept;
    private String companyPosition;
    //초대 코드
    private String invitationCode;

    //일반인 사용자
    public static MemberRequestDto ofGeneral(String email, String name, String password, String phoneNumber) {
        return MemberRequestDto.builder()
                .email(email)
                .name(name)
                .password(password)
                .phoneNumber(phoneNumber)
                .memberStatus("GENERAL")
                .build();
    }

    //회사 관리자 사용자
    public static MemberRequestDto ofManager(String email, String name, String password, String phoneNumber, String companyName, String companyDept, String companyPosition) {
        return MemberRequestDto.builder()
                .email(email)
                .name(name)
                .password(password)
                .phoneNumber(phoneNumber)
                .companyName(companyName)
                .companyDept(companyDept)
                .companyPosition(companyPosition)
                .memberStatus("MANAGER")
                .build();
    }

    //회사 직원 사용자
    public static MemberRequestDto ofEmployee(String email, String name, String password, String phoneNumber, String companyPosition, String invitationCode) {
        return MemberRequestDto.builder()
                .email(email)
                .name(name)
                .password(password)
                .phoneNumber(phoneNumber)
                .companyPosition(companyPosition)
                .invitationCode(invitationCode)
                .memberStatus("EMPLOYEE")
                .build();
    }

}
