package com.sparta.backoffice.global.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST : 잘못된 요청 */
    INVALID_VALUE(400, "유효하지 않은 값입니다."),
    INVALID_TOKEN(400, "유효하지 않은 토큰 입니다."),
    CAN_NOT_MODIFY_ERROR(400, "작성자만 수정할 수 있습니다."),
    CAN_NOT_DELETE_ERROR(400, "작성자만 삭제할 수 있습니다."),
    CAN_NOT_CHANGE_NICKNAME(400, "닉네임을 변경할 수 없습니다."),
    RECENTLY_USED_PASSWORD(400, "최근 사용한 비밀번호입니다."),
    CAN_NOT_READ_IMAGE(400, "게시글에 포함된 이미지를 처리할 수 없습니다."),
    CAN_NOT_FOLLOW_YOURSELF(400, "자기자신은 팔로우할 수 없습니다."),
    CAN_NOT_UNFOLLOW_YOURSELF(400, "자기자신은 언팔로우할 수 없습니다."),
    ALREADY_FOLLOW_USER(400, "이미 팔로우한 사용자입니다."),
    ALREADY_UNFOLLOW_USER(400, "이미 언팔로우한 사용자입니다."),
    NOT_EQUALS_ADMIN_TOKEN_ERROR(400, "관리자 암호가 틀려 등록이 불가능합니다."),

    /* 401 UNAUTHORIZED  :  인증 되지 않음 */
    NOT_VALID_TOKEN(401, "토큰이 유효하지 않습니다"),
    /* 403 FORBIDDEN  :  권한 없음 */
    DENIED_AUTHORITY(403, "권한이 없습니다."),
    SELF_LIKE_ERROR(400, "자신의 게시글은 좋아요 할 수 없습니다."),

    /* 404 UN_AUTHORIZED : 인증 불가 */
    UNAUTHORIZED(401, "인증 정보가 유효하지 않습니다."),

    /* 404 NOT_FOUND : Resource 권한이 없음 */
    ACCESS_DENIED(403, "권한이 없습니다."),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    CAN_NOT_REPLY_POST_ERROR(404, "존재 하지 않는 게시글에 답글을 달 수 없습니다."),
    NOT_FOUND_POST_ERROR(404, "존재하지 않는 게시글 입니다."),
    NOT_FOUND_USER_ERROR(404, "존재하지 않는 사용자 입니다."),
    NOT_FOUND_LIKE_ERROR(404, "존재하지 않는 좋아요 입니다."),

    /* 404 CONFLICT : Resource 중복 */
    ALREADY_EXIST_USERNAME(409, "아이디가 이미 존재합니다."),
    ALREADY_LIKED_ERROR(409, "이미 좋아요를 했습니다."),

    /* 500 INTERNAL_SERVER_ERROR : 서버 에러 */
    INTERNAL_SERVER_ERROR(500, "내부 서버 에러입니다.");

	private final int httpStatus;
    private final String message;
}
