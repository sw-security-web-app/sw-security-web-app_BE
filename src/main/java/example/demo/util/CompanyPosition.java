package example.demo.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CompanyPosition {
    CEO("사장"),
    VICE_PRESIDENT("부사장"),
    EXECUTIVE_DIRECTOR("전무"),
    MANAGING_DIRECTOR("상무"),
    DIRECTOR("이사"),
    GENERAL_MANAGER("부장"),
    DEPUTY_GENERAL_MANAGER("차장"),
    MANAGER("과장"),
    ASSISTANT_MANAGER("대리"),
    SENIOR_STAFF("주임"),
    STAFF("사원"),
    INTERN("인턴"),
    OTHER("기타");
    private final String position;
}
