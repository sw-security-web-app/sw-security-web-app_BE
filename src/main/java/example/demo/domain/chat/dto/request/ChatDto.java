package example.demo.domain.chat.dto.request;

import com.querydsl.core.annotations.QueryProjection;
import example.demo.domain.chat.AIModelType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatDto {

    private final Long chatId;

    @NotNull(message = "모델 타입을 입력해주세요.")
    private final AIModelType modelType;

    @NotBlank(message = "질문을 입력해주세요.")
    private final String question;

    private final String answer;

    @NotBlank
    private final Long chatRoomId;

    @Builder
    @QueryProjection
    public ChatDto(Long chatId, AIModelType modelType, String question, String answer, Long chatRoomId) {
        this.chatId = chatId;
        this.modelType = modelType;
        this.question = question;
        this.answer = answer;
        this.chatRoomId = chatRoomId;
    }
}
