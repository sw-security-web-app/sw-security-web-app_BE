package example.demo.domain.chat;

import example.demo.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatRoomErrorCode implements ErrorCode {

    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅방을 찾지 못했습니다. : Not Found Error"),
    INVALID_CHAT_DATA(HttpStatus.NOT_FOUND, "유효하지 않은 데이터입니다. : Invalid Data Error");

    private final HttpStatus httpStatus;
    private final String message;
}
