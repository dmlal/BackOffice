package com.sparta.backoffice.global.security.oauth.user;

import java.util.Map;

public class NaverOAuth2UserDetails extends OAuth2UserDetails {

	public NaverOAuth2UserDetails(Map<String, Object> attributes) {
		super((Map<String, Object>)attributes.get("response"));
	}

	@Override
	public String getId() {
		return String.valueOf(attributes.get("id"));
	}

	@Override
	public String getName() {
		return String.valueOf(attributes.get("name"));
	}

	@Override
	public String getEmail() {
		return String.valueOf(attributes.get("email"));
	}

	@Override
	public String getImageUrl() {
		return null;
	}

}
