package example.demo.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindEmailResponseDto {
    private String email;
    @Builder
    public FindEmailResponseDto(String email){
        this.email=email;
    }
}
