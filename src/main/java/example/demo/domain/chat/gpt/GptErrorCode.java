package example.demo.domain.chat.gpt;

import example.demo.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GptErrorCode implements ErrorCode {

    GPT_JSON_MAPPING_ERROR(HttpStatus.BAD_REQUEST, "Json 매핑 오류입니다 : 조회 실패"),
    GPT_JSON_PROCESS_ERROR(HttpStatus.BAD_REQUEST, "Json 프로세싱 오류입니다 : 조회 실패"),
    GPT_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류입니다 : 조회 실패");

    private final HttpStatus httpStatus;
    private final String message;
}
