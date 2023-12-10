package com.sparta.backoffice.global.security.oauth.user;

import java.util.Map;

public class KakaoOAuth2UserDetails extends OAuth2UserDetails {

	private final Map<String, Object> properties;

	public KakaoOAuth2UserDetails(Map<String, Object> attributes) {
		super(attributes);
		properties = (Map<String, Object>)attributes.get("properties");
	}

	@Override
	public String getId() {
		return String.valueOf(attributes.get("id"));
	}

	@Override
	public String getName() {
		return String.valueOf(properties.get("nickname"));
	}

	@Override
	public String getEmail() {
		return String.valueOf(properties.get("account_email"));
	}

	@Override
	public String getImageUrl() {
		return null;
	}
}
