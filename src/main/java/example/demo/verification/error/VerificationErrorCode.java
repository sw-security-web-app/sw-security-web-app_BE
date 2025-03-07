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
    ERROR_OF_CREATE_COMPANY(HttpStatus.INTERNAL_SERVER_ERROR,"회사 생성 중 오류가 발생했습니다."),
    TIME_OUT_ERROR(HttpStatus.REQUEST_TIMEOUT,"요청 시간이 초과했습니다. 다시 시도해주세요."),
    ERROR_OF_SEND_FILE_COMPANY(HttpStatus.INTERNAL_SERVER_ERROR,"회사 보안 데이터 전송 중 오류가 발생했습니다."),
    ERROR_OF_COMPANY(HttpStatus.INTERNAL_SERVER_ERROR,"보안 서버 내부 오류입니다."),
    ERROR_OF_CONVERTING_PDF(HttpStatus.BAD_REQUEST,"PDF 변환 도중 오류가 발새했습니다."),
    ERROR_OF_GET_COMPANY_STATUS(HttpStatus.INTERNAL_SERVER_ERROR,"회사의 검열 AI 생성 확인 중 오류가 발생했습니다."),
    NOT_CREATED_COMPANY(HttpStatus.BAD_REQUEST,"아직 검열 AI가 생성되지 않았습니다. 잠시 후 다시 시도해주세요.");
    private final HttpStatus httpStatus;
    private final String message;
}
