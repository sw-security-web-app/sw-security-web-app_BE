package example.demo.domain.member.dto.request;

import example.demo.util.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class MemberFindEmailRequestDto {
    @NotBlank(message = "휴대폰 번호는 필수 입력 값입니다.",
            groups = ValidationGroups.NotEmptyGroup.class)
    @Pattern(regexp = "^(010\\d{8}|\\d{3}\\d{7})$",
            message = "잘못된 휴대폰 번호 형식입니다.",
            groups = ValidationGroups.PatternCheckGroup.class)
    private String phoneNumber;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;
}
