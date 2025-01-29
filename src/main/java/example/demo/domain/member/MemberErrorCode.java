package example.demo.domain.member;

import example.demo.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum MemberErrorCode implements ErrorCode {
    INVALID_MAIL_NUMBER(HttpStatus.BAD_REQUEST,"잘못된 이메일 인증 번호입니다."),
    TIMEOUT_MAIL_NUMBER(HttpStatus.REQUEST_TIMEOUT,"이메일 인증 시간 초과입니다."),
    INVALID_MEMBER_STATUS(HttpStatus.BAD_REQUEST,"잘못된 유저 유형입니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
