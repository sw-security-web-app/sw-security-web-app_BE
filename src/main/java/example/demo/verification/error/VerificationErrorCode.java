package example.demo.verification.error;

import example.demo.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
@RequiredArgsConstructor
@Getter
public enum VerificationErrorCode implements ErrorCode {
    EMPTY_FILE_OR_TEXT(HttpStatus.BAD_REQUEST,"파일 또는 텍스트를 작성해주세요."),
    NO_FILE_TYPE(HttpStatus.BAD_REQUEST,"지원하지 않는 파일 유형입니다. (지원파일 유형 : .txt,pdf,.csv)"),
    ERROR_OF_CREATE_COMPANY(HttpStatus.INTERNAL_SERVER_ERROR,"AI에게 파일 전송 중 오류가 발생했습니다.\n 다시 시도해주세요.");
    private final HttpStatus httpStatus;
    private final String message;
}
