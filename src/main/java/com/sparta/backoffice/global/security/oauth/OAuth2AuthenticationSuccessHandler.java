package com.sparta.backoffice.global.security.oauth;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.backoffice.auth.dto.TokenDto;
import com.sparta.backoffice.auth.entity.RefreshToken;
import com.sparta.backoffice.auth.repository.RefreshTokenRepository;
import com.sparta.backoffice.global.constant.ResponseCode;
import com.sparta.backoffice.global.dto.BaseResponse;
import com.sparta.backoffice.global.security.CustomUserDetails;
import com.sparta.backoffice.global.util.JwtProvider;
import com.sparta.backoffice.user.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private final JwtProvider jwtProvider;
	private final RefreshTokenRepository refreshTokenRepository;
	private final ObjectMapper objectMapper;

	@Override
	public void onAuthenticationSuccess(
		HttpServletRequest request, HttpServletResponse response, Authentication authentication)
		throws IOException, ServletException {

		CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
		User user = userDetails.getUser();
		TokenDto tokenDto = jwtProvider.createToken(user.getUsername(), user.getRole());

		// DB 저장
		RefreshToken refreshToken = RefreshToken.of(user.getUsername(), tokenDto.getRefreshToken());
		refreshTokenRepository.save(refreshToken);

		//response 저장
		jwtProvider.setTokenResponse(tokenDto, response);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setStatus(ResponseCode.LOGIN.getHttpStatus());
		response.getWriter().write(
			objectMapper.writeValueAsString(
				BaseResponse.of(
					ResponseCode.LOGIN.getMessage(),
					ResponseCode.LOGIN.getHttpStatus(),
					""
				)
			)
		);
	}
}