package example.demo.domain.company;

import example.demo.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum CompanyErrorCode implements ErrorCode {
    NOT_EXIST_COMPANY(HttpStatus.BAD_REQUEST,"회사가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
