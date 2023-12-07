package com.sparta.backoffice.global.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.sparta.backoffice.auth.dto.TokenDto;
import com.sparta.backoffice.global.properties.JwtProperties;
import com.sparta.backoffice.user.constant.UserRoleEnum;
import com.sparta.backoffice.user.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "Jwt 유틸")
@RequiredArgsConstructor
@Component
public class JwtProvider {
	private static final String BEARER_PREFIX = "Bearer ";
	private static final String AUTHORIZATION_KEY = "auth";
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String REFRESH_TOKEN_HEADER = "RefreshToken";
	// 사용자 권한 값의 KEY
	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	private final JwtProperties jwtProperties;
	private String secretKey;
	private String adminKey;
	private Long accessTokenExpiration;
	private Long refreshTokenExpiration;
	private Key key;

	@PostConstruct
	public void init() {
		adminKey = jwtProperties.getAdminKey();
		secretKey = jwtProperties.getSecretKey();
		accessTokenExpiration = jwtProperties.getAccessTokenExpiration();
		refreshTokenExpiration = jwtProperties.getRefreshTokenExpiration();

		byte[] bytes = Base64.getDecoder().decode(secretKey);
		key = Keys.hmacShaKeyFor(bytes);
	}

	public String getTokenFromRequestHeader(HttpServletRequest req) {
		String tokenValue = req.getHeader(AUTHORIZATION_HEADER);

		if (!StringUtils.hasText(tokenValue)) {
			return null;
		}

		return subStringToken(tokenValue);
	}

	private String subStringToken(String tokenValue) {
		if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
			return tokenValue.substring(7);
		}

		log.error("Can not Substring Token Value");
		throw new IllegalArgumentException();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (SecurityException | MalformedJwtException | SignatureException e) {
			log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
		} catch (ExpiredJwtException e) {
			log.error("Expired JWT token, 만료된 JWT token 입니다.");
		} catch (UnsupportedJwtException e) {
			log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
		} catch (IllegalArgumentException e) {
			log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
		}
		return false;
	}

	// 토큰에서 사용자 정보 Claim 가져오기
	public Claims getUserInfoFromToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	//토큰 생성
	public TokenDto createToken(User user) {
		Date date = new Date();
		String username = user.getUsername();
		UserRoleEnum role = user.getRole();

		String accessToken =
				Jwts.builder()
						.setSubject(username) // 사용자 식별자값(ID)
						.claim(AUTHORIZATION_KEY, role) // 사용자 권한
						.setExpiration(new Date(date.getTime() + accessTokenExpiration)) // 만료 시간
						.setIssuedAt(date) // 발급일
						.signWith(key, signatureAlgorithm) // 암호화 알고리즘
						.compact();

		String refreshToken =
				Jwts.builder()
						.setSubject(username) // 사용자 식별자값(ID)
						.claim(AUTHORIZATION_KEY, role) // 사용자 권한
						.setExpiration(new Date(date.getTime() + refreshTokenExpiration)) // 만료 시간
						.setIssuedAt(date) // 발급일
						.signWith(key, signatureAlgorithm) // 암호화 알고리즘
						.compact();

		return TokenDto.of(accessToken, refreshToken);
	}

	public void setTokenResponse(TokenDto tokenDto, HttpServletResponse response) {
		setHeaderAccessToken(tokenDto.getAccessToken(), response);
		setCookieRefreshToken(tokenDto.getRefreshToken(), response);
	}

	private void setCookieRefreshToken(String refreshToken, HttpServletResponse response) {
		try {
			refreshToken = URLEncoder.encode(BEARER_PREFIX + refreshToken, "utf-8").replaceAll("\\+", "%20"); // Cookie Value 에는 공백이 불가능해서 encoding 진행

			Cookie cookie = new Cookie(REFRESH_TOKEN_HEADER, refreshToken); // Name-Value
			cookie.setSecure(true);
			cookie.setHttpOnly(true);
			cookie.setPath("/");

			// Response 객체에 Cookie 추가
			response.addCookie(cookie);
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
		}
	}

	private void setHeaderAccessToken(String accessToken, HttpServletResponse response) {
		response.setHeader(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken);
	}

	public String getAdminKey() {
		return adminKey;
	}
}
