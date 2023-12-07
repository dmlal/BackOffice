package com.sparta.backoffice.auth.service;

import static com.sparta.backoffice.global.constant.ErrorCode.*;

import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.backoffice.auth.dto.TokenDto;
import com.sparta.backoffice.auth.dto.request.LoginRequest;
import com.sparta.backoffice.auth.dto.request.SignupRequest;
import com.sparta.backoffice.auth.entity.Logout;
import com.sparta.backoffice.auth.entity.RefreshToken;
import com.sparta.backoffice.auth.repository.LogoutRepository;
import com.sparta.backoffice.auth.repository.RefreshTokenRepository;
import com.sparta.backoffice.global.constant.ErrorCode;
import com.sparta.backoffice.global.exception.ApiException;
import com.sparta.backoffice.global.security.CustomUserDetails;
import com.sparta.backoffice.global.util.JwtProvider;
import com.sparta.backoffice.user.constant.UserRoleEnum;
import com.sparta.backoffice.user.entity.User;
import com.sparta.backoffice.user.repository.UserRepository;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;
	private final RefreshTokenRepository refreshTokenRepository;
	private final LogoutRepository logoutRepository;

	@Transactional
	public void signup(SignupRequest request) {

		if (userRepository.existsByUsername(request.getUsername())) {
			throw new ApiException(ErrorCode.ALREADY_EXIST_USERNAME);
		}

		userRepository.save(request.toEntity(passwordEncoder));
	}

	@Transactional
	public void login(LoginRequest request, HttpServletResponse response) {
		User user = findUser(request);

		Authentication authentication = createAuthentication(request.getPassword(), user);
		setAuthentication(authentication);

		//Response 세팅
		TokenDto tokenDto = jwtProvider.createToken(user.getUsername(), user.getRole());
		jwtProvider.setTokenResponse(tokenDto, response);

		//Redis Refresh 토큰 저장
		refreshTokenRepository.save(RefreshToken.of(user.getUsername(), tokenDto.getRefreshToken()));
	}

	@Transactional
	public void reissue(HttpServletRequest request, HttpServletResponse response) {
		String targetToken = jwtProvider.getRefreshTokenFromCookie(request);

		//토큰 검증
		if (!jwtProvider.validateToken(targetToken)) {
			throw new ApiException(INVALID_TOKEN);
		}

		//토큰에서 username 추출
		Claims claims = jwtProvider.getUserInfoFromToken(targetToken);
		String username = claims.getSubject();
		UserRoleEnum role = UserRoleEnum.valueOf((String) claims.get("auth"));

		//db에서 refresh token 확인
		Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findById(username);

		//refresh token 일치하는지 검증
		if (refreshTokenOptional.isEmpty()) {
			throw new ApiException(INVALID_TOKEN);
		}

		//토큰 재발급
		TokenDto tokenDto = jwtProvider.createToken(username, role);
		jwtProvider.setTokenResponse(tokenDto, response);

		//재발급된 토큰 정보 저장
		refreshTokenRepository.save(RefreshToken.of(username, tokenDto.getRefreshToken()));
	}

	//내부 사용
	public User findUser(LoginRequest request) {
		return userRepository.findByUsername(request.getUsername())
			.orElseThrow(() -> new ApiException(NOT_FOUND_USER));
	}

	private void checkPassword(String rawPassword, String encodedPassword) {
		if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
			throw new ApiException(NOT_FOUND_USER);
		}
	}

	// 인증 객체 생성
	private Authentication createAuthentication(String rawPassword, User user) {
		checkPassword(rawPassword, user.getPassword());

		CustomUserDetails userDetails = new CustomUserDetails(user);
		return new UsernamePasswordAuthenticationToken(userDetails, user.getPassword(), userDetails.getAuthorities());
	}

	private void setAuthentication(Authentication authentication) {
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(authentication);
	}

	@Transactional
	public void logout(HttpServletRequest request) {
		String accessToken = jwtProvider.getTokenFromRequestHeader(request);

		jwtProvider.validateToken(accessToken);

		//토큰에서 username, expiration 추출
		Claims claims = jwtProvider.getUserInfoFromToken(accessToken);
		String username = claims.getSubject();

		//로그아웃 처리
		refreshTokenRepository.deleteById(username);
		logoutRepository.save(Logout.of(accessToken, username));
	}
}
