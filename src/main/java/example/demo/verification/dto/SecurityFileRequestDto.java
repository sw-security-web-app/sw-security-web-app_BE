package example.demo.verification.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
public class SecurityFileRequestDto {
    private String content;
}
