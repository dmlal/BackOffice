package com.sparta.backoffice.auth.service;

import static com.sparta.backoffice.global.constant.ErrorCode.*;

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
import com.sparta.backoffice.global.constant.ErrorCode;
import com.sparta.backoffice.global.exception.ApiException;
import com.sparta.backoffice.global.security.CustomUserDetails;
import com.sparta.backoffice.global.util.JwtProvider;
import com.sparta.backoffice.user.entity.User;
import com.sparta.backoffice.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;

	@Transactional
	public void signup(SignupRequest request) {

		if (userRepository.existsByUsername(request.getUsername())) {
			throw new ApiException(ErrorCode.ALREADY_EXIST_USERNAME);
		}

		userRepository.save(request.toEntity(passwordEncoder));
	}

	public void login(LoginRequest request, HttpServletResponse response) {
		User user = findUser(request);

		Authentication authentication = createAuthentication(request.getPassword(), user);
		setAuthentication(authentication);

		//Response 세팅
		TokenDto tokenDto = jwtProvider.createToken(user);
		jwtProvider.setTokenResponse(tokenDto, response);
	}


	public User findUser(LoginRequest request) {
		return userRepository.findByUsername(request.getUsername())
			.orElseThrow(() -> new ApiException(USER_NOT_FOUND));
	}

	private void checkPassword(String rawPassword, String encodedPassword) {
		if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
			throw new ApiException(USER_NOT_FOUND);
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
}
