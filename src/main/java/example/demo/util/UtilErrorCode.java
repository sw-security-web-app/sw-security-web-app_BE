package example.demo.util;

import example.demo.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum UtilErrorCode implements ErrorCode {
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"코드 생성 중 서버 오류 발생"),
    PROMPT_CENSOR_ERROR(HttpStatus.BAD_REQUEST, "Prompt 검열 오류입니다. : Prompt Censor Error"),
    PYTHON_SERVER_ERROR(HttpStatus.BAD_REQUEST, "Python 서버 오류입니다. : Python Server Error");

    private final HttpStatus httpStatus;
    private final String message;
}
