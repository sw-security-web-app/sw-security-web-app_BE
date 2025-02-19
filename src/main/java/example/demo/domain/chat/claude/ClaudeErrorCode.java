package example.demo.domain.chat.claude;

import example.demo.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ClaudeErrorCode implements ErrorCode {

    NO_EXIST_BODY(HttpStatus.BAD_REQUEST, "응답에 실패했습니다. : Body Not Exist"),
    NO_EXIST_CONTENT(HttpStatus.NO_CONTENT, "응답에 실패했습니다. : Content Not Exist");

    private final HttpStatus httpStatus;
    private final String message;
}
