package example.demo.security.auth.dto;

import example.demo.domain.member.MemberStatus;
import example.demo.util.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
public class CustomMemberInfoDto extends MemberLoginDto {
    private Long memberId;
    private MemberStatus memberStatus;
    private boolean accountLocked;
    //로그인 실패 횟수
    private int failedAttempts;

    @Builder
    public CustomMemberInfoDto(Long memberId,@NotBlank(message = "이메일은 필수 입력 값입니다.",
            groups = ValidationGroups.NotEmptyGroup.class) @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
            message = "잘못된 이메일 입력입니다.",
            groups = ValidationGroups.PatternCheckGroup.class
    ) String email, String password,MemberStatus memberStatus,boolean accountLocked ) {
        super(email, password);
        this.memberId=memberId;
        this.memberStatus=memberStatus;
        this.accountLocked=accountLocked;
    }
}
