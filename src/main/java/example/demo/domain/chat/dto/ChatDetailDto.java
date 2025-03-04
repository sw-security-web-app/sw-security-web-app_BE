package example.demo.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder

public class ChatDetailDto {
    private Long chatId;
    private  String question;
    private  String answer;
    private  String answerDateTime;
    private  String questionDateTime;

    /**
     *
     * @param chatId 채팅 고유 Index
     * @param question 질문 내용
     * @param answer 답변 내용
     * @param answerDateTime 답변 시간
     * @param questionDateTime 질문 시간
     * @return ChatDetailDto
     */
    public ChatDetailDto of(Long chatId, String question, String answer, LocalDateTime answerDateTime, LocalDateTime questionDateTime){
        return ChatDetailDto
                .builder()
                .answer(answer)
                .answerDateTime(parsedLocalDateTime(answerDateTime))
                .question(question)
                .chatId(chatId)
                .questionDateTime(parsedLocalDateTime(questionDateTime))
                .build();

    }

    /**
     *
     * @param dateTime LocalDateTime 형태의 시간
     * @return String 형태의 시간 ex)2022-12-22 12:12:11
     */
    private String parsedLocalDateTime(LocalDateTime dateTime){
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
