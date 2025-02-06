package example.demo.domain.member.dto.request;

import example.demo.util.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberLoginDto {
    @NotBlank(message = "이메일은 필수 입력 값입니다.",
            groups = ValidationGroups.NotEmptyGroup.class)
    @Pattern(regexp ="^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
            message = "잘못된 이메일 입력입니다.",
            groups = ValidationGroups.PatternCheckGroup.class
    )
    private String email;

    private String password;

    @Builder
    public MemberLoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
