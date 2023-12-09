package com.sparta.backoffice.global.security.oauth;

import static com.sparta.backoffice.global.constant.ErrorCode.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.backoffice.global.dto.BaseResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	private final ObjectMapper objectMapper;

	public OAuth2AuthenticationFailureHandler(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception)
		throws IOException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setStatus(INTERNAL_SERVER_ERROR.getHttpStatus());
		response.getWriter().write(
			objectMapper.writeValueAsString(
				BaseResponse.of(
					INTERNAL_SERVER_ERROR.getMessage(),
					INTERNAL_SERVER_ERROR.getHttpStatus(),
					""
				)
			)
		);
	}
}