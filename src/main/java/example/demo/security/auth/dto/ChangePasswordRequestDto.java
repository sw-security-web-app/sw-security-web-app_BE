package example.demo.security.auth.dto;

import example.demo.util.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ChangePasswordRequestDto extends MemberLoginDto{
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.",
            groups = ValidationGroups.NotEmptyGroup.class)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "잘못된 비밀번호 형식입니다.",
            groups = ValidationGroups.PatternCheckGroup.class)
    private String newPassword;
}
