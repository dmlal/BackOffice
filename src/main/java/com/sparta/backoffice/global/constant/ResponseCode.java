package com.sparta.backoffice.global.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    /* 200 OK */
    OK(200, "요청 성공"),
    GET_LIKE_USERS(200, "게시글 좋아요한 유저 목록 조회 성공"),
    DELETED_POST(200, "게시글 삭제 성공"),
    GET_USER_POSTS(200, "번 유저의 게시글 목록 조회 성공"),
    GET_POST_DETAIL(200, "게시글 상세 조회 성공"),
    UPDATE_PROFILE(200, "프로필 정보 변경 성공"),
    UPDATE_PASSWORD(200, "비밀번호 변경 성공"),
    MODIFIED_POST(200, "게시글 수정 성공"),

    /* 201 CREATED : Resource 생성 완료 */
    SIGNUP(201, "회원가입 성공"),
    LOGIN(201, "로그인 성공"),
    CREATED_POST(201, "게시글 작성 성공"),
    CREATED_LIKE(201, "좋아요 성공"),
    REISSUE_TOKEN(201, "토큰 재발급 성공 "),
  
    /* 204 NO-CONTENT: Resource 없음 */
    DELETED_LIKE(204, "좋아요 취소 성공"),
    LOGOUT(204, "로그아웃 성공");

  
    private final int httpStatus;
    private final String message;
}
