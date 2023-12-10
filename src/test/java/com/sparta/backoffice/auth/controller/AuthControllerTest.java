package com.sparta.backoffice.auth.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.backoffice.auth.dto.TokenDto;
import com.sparta.backoffice.auth.dto.request.LoginRequest;
import com.sparta.backoffice.helper.JwtAuthenticationHelper;

import jakarta.servlet.http.Cookie;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	JwtAuthenticationHelper jwtAuthenticationHelper;
	@Autowired
	ObjectMapper objectMapper;

	@Test
	@Transactional
	void 로그인_테스트() throws Exception {
		//Given
		String username = "testuser1";
		String password = "testUser1!";
		LoginRequest request = new LoginRequest(username, password);
		jwtAuthenticationHelper.createMember(username, password);
		//When
		ResultActions actions = mvc.perform(
			post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
		);

		//Then
		MockHttpServletResponse response = actions
			.andDo(print())
			.andReturn()
			.getResponse();

		assertThat(response.getHeader("Authorization")).startsWith("Bearer");
		assertThat(Objects.requireNonNull(response.getCookie("RefreshToken")).getValue()).startsWith("Bearer");
	}

	@Test
	@Transactional
	void 재발급_테스트() throws Exception {
		//Given
		String username = "testuser1";
		String password = "testUser1!";
		LoginRequest request = new LoginRequest(username, password);
		TokenDto dto = jwtAuthenticationHelper.login(request);

		//When
		ResultActions actions = mvc.perform(
			post("/api/auth/reissue")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", dto.getAccessToken())
				.cookie(new Cookie("RefreshToken", dto.getRefreshToken()))
		);

		MockHttpServletResponse response = actions
			.andDo(print())
			.andReturn()
			.getResponse();

		//Then
		actions
			.andExpect(status().isCreated());
	}
}