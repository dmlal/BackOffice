package com.sparta.backoffice.global.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "jwt")
@Validated
public class JwtProperties {

	@NotBlank
	private final String secretKey;
	private final Long accessTokenExpiration;
	private final Long refreshTokenExpiration;
}
