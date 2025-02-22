package example.demo.domain.chat.dto;

import com.querydsl.core.annotations.QueryProjection;
import example.demo.domain.chat.AIModelType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatDto {

    private final Long chatId;
    private final AIModelType modelType;
    private final String question;
    private final String answer;
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
