package example.demo.domain.member;

import example.demo.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum MemberErrorCode implements ErrorCode {
    INVALID_MAIL_NUMBER(HttpStatus.BAD_REQUEST,"잘못된 이메일 인증 번호입니다."),
    TIMEOUT_MAIL_NUMBER(HttpStatus.REQUEST_TIMEOUT,"이메일 인증 시간 초과입니다."),
    INVALID_MEMBER_STATUS(HttpStatus.BAD_REQUEST,"잘못된 유저 유형입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 유저입니다."),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST,"중복된 이메일입니다."),
    INVALID_CERTIFICATION_CODE(HttpStatus.BAD_REQUEST,"인증번호가 일치하지 않습니다."),
    INVALID_CERTIFICATION_EMAIL_OR_PHONE(HttpStatus.UNAUTHORIZED,"이메일 인증 또는 휴대폰 인증이 유효하지 않습니다."),
    INVALID_PERMISSION(HttpStatus.BAD_GATEWAY,"접근권한이 없습니다."),

    EMPTY_PHONENUMBER(HttpStatus.BAD_REQUEST,"휴대폰 번호를 입력해주세요."),
    INVALID_PHONENUMBER(HttpStatus.BAD_REQUEST,"올바르지 않은 휴대폰 형식입니다."),
    INVALID_PHONE_CERTIFICATION_NUMBER(HttpStatus.UNAUTHORIZED,"휴대폰 인증번호가 유효하지 않습니다."),
    DUPLICATED_PHONENUMBER(HttpStatus.BAD_REQUEST,"중복된 휴대폰 번호입니다.");

  

    private final HttpStatus httpStatus;
    private final String message;
}
