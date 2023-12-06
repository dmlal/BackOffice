package com.sparta.backoffice.global.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    /* 201 CREATED : Resource 생성 완료 */
    SIGNUP(201, "회원가입 성공"),
    LOGIN(201, "로그인 성공"),
    CREATED_POST(201, "게시글 작성 성공"),
    MODIFIYED_POST(200, "게시글 수정 성공"),
    DELETED_POST(200, "게시글 삭제 성공");;

    private final int httpStatus;
    private final String message;
}
