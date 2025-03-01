package example.demo.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ResponseDto {
    private final Integer code;
    private final String message;

    public static ResponseDto of(Integer code,String message){
        return ResponseDto
                .builder()
                .code(code)
                .message(message)
                .build();
    }
}
