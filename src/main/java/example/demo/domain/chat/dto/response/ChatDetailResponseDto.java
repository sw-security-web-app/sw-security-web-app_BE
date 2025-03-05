package example.demo.domain.chat.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class ChatDetailResponseDto {
    private Long chatId;
    private Message message;
    private String createdDate;

    /**
     * @param chatId      채팅 고유 Index
     * @param question    질문 내용
     * @param answer      답변 내용
     * @param createdDate 생성된 시간
     */
    @QueryProjection
    public ChatDetailResponseDto(Long chatId, String question, String answer, LocalDateTime createdDate) {
        this.chatId = chatId;
        this.message = new Message(question, answer);
        this.createdDate = parsedLocalDateTime(createdDate);
    }

    /**
     * @param dateTime LocalDateTime 형태의 시간
     * @return String 형태의 시간 ex)2022-12-22 12:12:11
     */
    private String parsedLocalDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Getter
    public static class Message {
        private final String question;
        private final String answer;

        Message(String question, String answer) {
            this.answer = answer;
            this.question = question;
        }
    }
}
