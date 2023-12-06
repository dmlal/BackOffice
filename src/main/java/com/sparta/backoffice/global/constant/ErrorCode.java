package com.sparta.backoffice.global.constant;


import jakarta.persistence.CollectionTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST : 잘못된 요청 */
    INVALID_VALUE(400, "유효하지 않은 값입니다."),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    USER_NOT_FOUND(404, "유저를 찾을 수 없습니다."),

    /* 404 CONFLICT : Resource 중복 */
    ALREADY_EXIST_USERNAME(409, "아이디가 이미 존재합니다." ),

    /* 500 INTERNAL_SERVER_ERROR : 서버 에러 */
    INTERNAL_SERVER_ERROR(500, "내부 서버 에러입니다.");

    private final int httpStatus;
    private final String message;
}
