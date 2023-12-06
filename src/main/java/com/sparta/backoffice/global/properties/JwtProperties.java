package com.sparta.backoffice.global.properties;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@NoArgsConstructor
@Setter
@ConfigurationProperties(prefix = "jwt")
@Validated
public class JwtProperties {

	@NotBlank
	private String secretKey;
	private Long accessTokenExpiration;
	private Long refreshTokenExpiration;
}
