package com.sparta.backoffice.helper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.sparta.backoffice.auth.dto.TokenDto;
import com.sparta.backoffice.auth.dto.request.LoginRequest;
import com.sparta.backoffice.auth.entity.RefreshToken;
import com.sparta.backoffice.auth.repository.RefreshTokenRepository;
import com.sparta.backoffice.global.util.JwtProvider;
import com.sparta.backoffice.user.constant.UserRoleEnum;
import com.sparta.backoffice.user.entity.User;
import com.sparta.backoffice.user.repository.UserRepository;

@Component
public class JwtAuthenticationHelper {

	@Autowired
	private JwtProvider jwtProvider;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	private static final String BEARER_PREFIX = "Bearer ";

	public User createMember(String username, String password) {
		User user = new User(
			username,
			passwordEncoder.encode(password),
			UserRoleEnum.USER
		);
		return userRepository.save(user);
	}

	public TokenDto login(LoginRequest loginRequest) {
		User user = createMember(loginRequest.getUsername(), loginRequest.getPassword());
		TokenDto dto = jwtProvider.createToken(user.getUsername(), user.getRole());
		String accessToken = BEARER_PREFIX + dto.getAccessToken();
		String refreshToken = URLEncoder.encode(BEARER_PREFIX + dto.getRefreshToken(), StandardCharsets.UTF_8)
			.replaceAll("\\+", "%20");

		refreshTokenRepository.save(new RefreshToken(user.getUsername(), refreshToken));

		return new TokenDto(
			accessToken,
			refreshToken
		);
	}
}
