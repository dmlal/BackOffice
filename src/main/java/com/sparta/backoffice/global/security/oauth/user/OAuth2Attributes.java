package com.sparta.backoffice.global.security.oauth.user;

import static com.sparta.backoffice.global.constant.SocialType.*;

import java.util.Map;

import com.sparta.backoffice.global.constant.SocialType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OAuth2Attributes {
	Map<String, Object> attributes;
	String id;
	String name;
	String email;


	public static OAuth2Attributes of(SocialType socialType, Map<String, Object> attributes) {
		switch (socialType) {
			case NAVER -> {
				Map<String, Object> response = (Map<String, Object>) attributes.get("response");
				if (response == null) {
					return null;
				}
				return OAuth2Attributes.builder()
					.attributes(attributes)
					.id(NAVER.getCode() + response.get("id"))
					.name((String) response.get("name"))
					.email((String) response.get("email"))
					.build();
			}

			case KAKAO -> {
				Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
				if (properties == null) {
					return null;
				}
				return OAuth2Attributes.builder()
					.attributes(attributes)
					.id(KAKAO.getCode() + attributes.get("id"))
					.name((String) properties.get("name"))
					.email((String) properties.get("account_email"))
					.build();
			}
			default -> throw new IllegalArgumentException("Invalid Provider Type.");
		}
	}
}
