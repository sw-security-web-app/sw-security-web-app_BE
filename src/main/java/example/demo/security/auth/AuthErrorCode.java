package example.demo.security.auth;

import example.demo.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum AuthErrorCode implements ErrorCode {
    NOT_EXIST_EMAIL(HttpStatus.NOT_FOUND,"이메일이 존재하지 않습니다."),
    INVALID_EMAIL_OR_PASSWORD(HttpStatus.NOT_FOUND,"이메일 또는 비밀번호가 일치하지 않습니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
