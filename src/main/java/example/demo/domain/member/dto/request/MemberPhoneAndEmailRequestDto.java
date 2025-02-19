package example.demo.domain.member.dto.request;

import example.demo.util.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberPhoneAndEmailRequestDto {
    @NotBlank(message = "휴대폰 번호는 필수 입력 값입니다.",
            groups = ValidationGroups.NotEmptyGroup.class)
    @Pattern(regexp = "^(010\\d{8}|\\d{3}\\d{7})$",
            message = "잘못된 휴대폰 번호 형식입니다.",
            groups = ValidationGroups.PatternCheckGroup.class)
    private String phoneNumber;

    @NotBlank(message = "이메일은 필수 입력 값입니다.",
            groups = ValidationGroups.NotEmptyGroup.class)
    @Pattern(regexp ="^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
            message = "잘못된 이메일 입력입니다.",
            groups = ValidationGroups.PatternCheckGroup.class
    )
    private String email;
}
