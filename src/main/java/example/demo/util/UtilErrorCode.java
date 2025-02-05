package example.demo.util;

import example.demo.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum UtilErrorCode implements ErrorCode {
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"코드 생성 중 서버 오류 발생");
    private final HttpStatus httpStatus;
    private final String message;
}
