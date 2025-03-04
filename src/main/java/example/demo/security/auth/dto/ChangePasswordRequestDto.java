package example.demo.security.auth.dto;

import example.demo.util.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChangePasswordRequestDto extends MemberLoginDto{
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.",
            groups = ValidationGroups.NotEmptyGroup.class)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "잘못된 비밀번호 형식입니다.",
            groups = ValidationGroups.PatternCheckGroup.class)
    private String newPassword;

    @Builder
    public ChangePasswordRequestDto (@NotBlank(message = "이메일은 필수 입력 값입니다.",
            groups = ValidationGroups.NotEmptyGroup.class) @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
            message = "잘못된 이메일 입력입니다.",
            groups = ValidationGroups.PatternCheckGroup.class
    ) String email,String password,String newPassword){
        super(email,password);
        this.newPassword=newPassword;
    }
}


