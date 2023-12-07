package com.sparta.backoffice.global.properties;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@ConfigurationProperties(prefix = "jwt")
@RequiredArgsConstructor
@Validated
public class JwtProperties {

    @NotBlank
    private final String adminKey;
    private final String secretKey;
    private final Long accessTokenExpiration;
    private final Long refreshTokenExpiration;
}
