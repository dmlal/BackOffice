package com.sparta.backoffice.global.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

	/* 201 CREATED : Resource 생성 완료 */
	SIGNUP (201, "회원가입 성공"),
	LOGIN (201, "로그인 성공");

	private final int httpStatus;
	private final String message;
}
