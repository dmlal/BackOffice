package com.sparta.backoffice.global.security.oauth.user;

import java.util.Map;

import com.sparta.backoffice.global.constant.SocialType;

public class OAuth2UserInfoFactory {
	public static OAuth2UserDetails getOAuth2UserInfo(SocialType socialType, Map<String, Object> attributes) {
		return switch (socialType) {
			case NAVER -> new NaverOAuth2UserDetails(attributes);
			case KAKAO -> new KakaoOAuth2UserDetails(attributes);
		};
	}
}
