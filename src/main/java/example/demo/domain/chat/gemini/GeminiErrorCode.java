package example.demo.domain.chat.gemini;

import example.demo.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GeminiErrorCode implements ErrorCode {

    GEMINI_JSON_PROCESS_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Json 프로세싱 오류입니다 : Json Processing Error"),
    GEMINI_NO_CANDIDATES(HttpStatus.INTERNAL_SERVER_ERROR, "Candidates를 찾지 못했습니다 : Server Error"),
    GEMINI_NO_TEXT_PART(HttpStatus.INTERNAL_SERVER_ERROR, "응답 Text를 찾지 못했습니다 : Server Error");

    private final HttpStatus httpStatus;
    private final String message;
}
