package com.sparta.backoffice.global.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    USER_NOT_FOUND(404, "유저를 찾을 수 없습니다."),
    /* 500 INTERNAL_SERVER_ERROR : 서버 에러 */
    INTERNAL_SERVER_ERROR(500, "내부 서버 에러입니다."),
    NOT_FOUND_POST_ERROR(404, "존재하지 않는 게시글 입니다."),
    NOT_FOUND_USER_ERROR(404, "존재하지 않는 사용자 입니다.")
    ;


    private final int httpStatus;
    private final String message;
}
