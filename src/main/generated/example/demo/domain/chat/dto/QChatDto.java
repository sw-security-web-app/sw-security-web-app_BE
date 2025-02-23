package example.demo.domain.chat.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * example.demo.domain.chat.dto.QChatDto is a Querydsl Projection type for ChatDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QChatDto extends ConstructorExpression<ChatDto> {

    private static final long serialVersionUID = -546713723L;

    public QChatDto(com.querydsl.core.types.Expression<Long> chatId, com.querydsl.core.types.Expression<example.demo.domain.chat.AIModelType> modelType, com.querydsl.core.types.Expression<String> question, com.querydsl.core.types.Expression<String> answer) {
        super(ChatDto.class, new Class<?>[]{long.class, example.demo.domain.chat.AIModelType.class, String.class, String.class}, chatId, modelType, question, answer);
    }

}

