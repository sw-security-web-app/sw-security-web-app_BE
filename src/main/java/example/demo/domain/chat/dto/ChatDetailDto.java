package example.demo.domain.chat.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class ChatDetailDto {
    private Long chatId;
    private  String question;
    private  String answer;
    private  String createdDate;

    /**
     *
     * @param chatId 채팅 고유 Index
     * @param question 질문 내용
     * @param answer 답변 내용
     * @param createdDate 생성된 시간
     */
    @QueryProjection
    public ChatDetailDto(Long chatId, String question, String answer, LocalDateTime createdDate){
        this.chatId=chatId;
        this.answer=answer;
        this.question=question;
        this.createdDate=parsedLocalDateTime(createdDate);
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
