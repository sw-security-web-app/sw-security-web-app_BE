package example.demo.security.auth;

import example.demo.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum AuthErrorCode implements ErrorCode {
    NOT_EXIST_EMAIL(HttpStatus.NOT_FOUND,"이메일이 존재하지 않습니다."),
    INVALID_EMAIL_OR_PASSWORD(HttpStatus.NOT_FOUND,"이메일 또는 비밀번호가 일치하지 않습니다."),
    NOT_EXIST_MEMBER(HttpStatus.NOT_FOUND,"유저가 존재하지 않습니다."),
    EXCEED_LOGIN(HttpStatus.UNAUTHORIZED,"로그인 횟수를 5회 초과했습니다.30분 후 다시 시도해주세요."),
    NO_AUTHORITIES(HttpStatus.FORBIDDEN,"권한이 없습니다."),
    NOT_AUTHENTICATED_REQUEST(HttpStatus.UNAUTHORIZED,"인증된 유저가 아닙니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_GATEWAY,"유효하지 않은 리프레시 토큰값입니다."),
    CAN_NOT_LOCKED_MANAGER(HttpStatus.BAD_REQUEST,"관리자의 계정은 잠금처리할 수 없습니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST,"올바르지 않은 타입 값입니다."),
    IS_LOCKED(HttpStatus.BAD_REQUEST,"해당 계정이 잠긴 상태입니다."),
    IS_NOT_LOCKED(HttpStatus.BAD_REQUEST,"해당 계정은 잠긴 상태가 아닙니다."),
    LOCKED_ACCOUT(HttpStatus.LOCKED,"로그인 5회 초과로 계정이 잠겼습니다. \n관리자에게 문의하세요.");

    private final HttpStatus httpStatus;
    private final String message;
}
