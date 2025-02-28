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
    private final String code;
    private final String message;

    ResponseDto of(String code,String message){
        return ResponseDto
                .builder()
                .code(code)
                .message(message)
                .build();
    }
}
