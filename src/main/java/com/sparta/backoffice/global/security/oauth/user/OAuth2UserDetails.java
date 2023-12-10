package com.sparta.backoffice.global.security.oauth.user;

import java.util.Map;

public abstract class OAuth2UserDetails {
	protected Map<String, Object> attributes;

	protected OAuth2UserDetails(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public abstract String getId();

	public abstract String getName();

	public abstract String getEmail();

	public abstract String getImageUrl();
}