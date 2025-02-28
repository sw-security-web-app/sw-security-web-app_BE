package example.demo.domain.company.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
public class SecurityFileRequestDto {
    private String content;
}
