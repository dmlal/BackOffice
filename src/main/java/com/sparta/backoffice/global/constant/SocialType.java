package com.sparta.backoffice.global.constant;

import lombok.Getter;

@Getter
public enum SocialType {
	NAVER("네이버", "NAVER_"),
	KAKAO("카카오", "KAKAO_");

	private final String ko;
	private final String code;

	SocialType(String ko, String code) {
		this.ko = ko;
		this.code = code;
	}
}
