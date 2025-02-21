package example.demo.security.exception;

import example.demo.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
@RequiredArgsConstructor
@Getter
public enum SecurityErrorCode implements ErrorCode {
    NOT_EXIST_REFRESH_TOKEN(HttpStatus.NOT_FOUND,"토큰이 존재하지 않습니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST,"유효하지 않은 토큰입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
