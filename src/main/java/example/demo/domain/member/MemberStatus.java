package example.demo.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberStatus {
    GENERAL("일반인"),
    MANAGER("관리자"),
    EMPLOYEE("직원");

    private final String text;

}
