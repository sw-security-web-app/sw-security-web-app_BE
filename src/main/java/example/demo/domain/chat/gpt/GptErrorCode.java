package example.demo.domain.chat.gpt;

import example.demo.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GptErrorCode implements ErrorCode {

    GPT_JSON_MAPPING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Json 매핑 오류입니다 : Json Mapping Error"),
    GPT_JSON_PROCESS_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Json 프로세싱 오류입니다 : Json Processing Error"),
    GPT_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류입니다 : Internal API Server Error"),
    GPT_NO_RESPONSE(HttpStatus.NOT_FOUND, "GPT 응답 오류입니다 : NO Response Error"),
    GPT_NO_CONTENT(HttpStatus.NO_CONTENT, "GPT Content 오류입니다. : NO Content Error");

    private final HttpStatus httpStatus;
    private final String message;
}
