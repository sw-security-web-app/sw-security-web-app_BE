package example.demo.verification.error;

import example.demo.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
@RequiredArgsConstructor
@Getter
public enum VerificationErrorCode implements ErrorCode {
    EMPTY_FILE_OR_TEXT(HttpStatus.BAD_REQUEST,"파일 또는 텍스트를 작성해주세요."),
    NO_FILE_TYPE(HttpStatus.BAD_REQUEST,"지원하지 않는 파일 유형입니다. (지원파일 유형 : .txt,pdf,.csv)");
    private final HttpStatus httpStatus;
    private final String message;
}
