package example.demo.util.pdf;

import example.demo.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PdfErrorCode implements ErrorCode {
    INVALID_PERMISSION(HttpStatus.BAD_REQUEST,"업로드 권한이 없습니다."),
    FAILED_TO_UPLOAD(HttpStatus.BAD_REQUEST,"업로드를 실패했습니다."),
    FILE_SIZE_OVER(HttpStatus.BAD_REQUEST,"파일의 크기를 초과했습니다.(최대 30MB)"),
    CONVERSION_FAILED(HttpStatus.BAD_REQUEST,"파일 전환에 실패했습니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
