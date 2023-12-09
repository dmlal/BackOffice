package com.sparta.backoffice.global.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    /* 200 OK */
    OK(200, "요청 성공"),
    GET_LIKE_USERS(200, "게시글 좋아요한 유저 목록 조회 성공"),
    GET_USER_POSTS(200, "번 유저의 게시글 목록 조회 성공"),
    GET_POST_DETAIL(200, "게시글 상세 조회 성공"),
    GET_USER(200, "유저 정보 조회 성공"),
    GET_ALL_USER(200, "모든 유저 조회 성공"),
    GET_LIKE_POSTS(200, "좋아요를 누른 게시글 목록 조회 성공"),
    GET_FOLLOWING_POSTS(200, "팔로잉중인 유저들의 게시글 목록 조회 성공"),
    FOLLOW_USER(200, "유저 팔로우"),
    UNFOLLOW_USER(200, "유저 언팔로우"),
    GET_FOLLOWER_LIST(200, "팔로워 목록 보기"),
    GET_FOLLOWING_LIST(200, "팔로잉 목록 보기"),
    MODIFIED_POST(200, "게시글 수정 성공"),
    UPDATE_PROFILE(200, "프로필 정보 변경 성공"),
    UPDATE_PASSWORD(200, "비밀번호 변경 성공"),
    DELETED_POST(200, "게시글 삭제 성공"),
    DELETED_USER(200, "회원 탈퇴 성공"),
    DELETED_LIKE(200, "좋아요 취소 성공"),
    LOGOUT(200, "로그아웃 성공"),
    UPDATE_PROFILE_IMAGE(200, "프로필 이미지 변경 완료"),
    BLOCK_USER(200, "회원 차단 성공"),
    UNBLOCK_USER(200, "회원 차단 해지 성공"),

    /* 201 CREATED : Resource 생성 완료 */
    SIGNUP(201, "회원가입 성공"),
    LOGIN(201, "로그인 성공"),
    CREATED_POST(201, "게시글 작성 성공"),
    CREATED_LIKE(201, "좋아요 성공"),
    REISSUE_TOKEN(201, "토큰 재발급 성공 ");

    private final int httpStatus;
    private final String message;
}
