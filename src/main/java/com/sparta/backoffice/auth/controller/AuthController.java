package com.sparta.backoffice.auth.controller;

import static com.sparta.backoffice.global.constant.ResponseCode.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.backoffice.auth.dto.request.LoginRequest;
import com.sparta.backoffice.auth.dto.request.SignupRequest;
import com.sparta.backoffice.auth.service.AuthService;
import com.sparta.backoffice.global.dto.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Tag(name = "인증 API", description = "인증 API 컨트롤러")
@RequestMapping("/api/auth")
@RestController
public class AuthController {
	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@Operation(summary = "회원 가입", description = "회원 가입 API")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "201",
			description = "회원가입 성공",
			content = @Content(schema = @Schema(implementation = BaseResponse.class))
		),
		@ApiResponse(
			responseCode = "409",
			description = "회원가입 실패 - 이미 아이디가 존재하는 경우",
			content = @Content(schema = @Schema(implementation = BaseResponse.class))
		)
	})
	@PostMapping("/signup")
	public ResponseEntity<BaseResponse<Void>> signup(@Valid @RequestBody SignupRequest request) {
		authService.signup(request);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(
				BaseResponse.of(
					SIGNUP.getMessage(),
					SIGNUP.getHttpStatus(),
					null
				)
			);
	}

	@Operation(summary = "로그인", description = "로그인 API")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "201",
			description = "로그인 성공",
			content = @Content(schema = @Schema(implementation = BaseResponse.class))
		),
		@ApiResponse(
			responseCode = "404",
			description = "로그인 실패 - 아이디가 존재하지 않은 경우, 비밀번호가 다른경우",
			content = @Content(schema = @Schema(implementation = BaseResponse.class))
		)
	})
	@PostMapping("/login")
	public ResponseEntity<BaseResponse<Void>> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
		authService.login(request, response);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(
				BaseResponse.of(
					LOGIN.getMessage(),
					LOGIN.getHttpStatus(),
					null
				)
			);
	}
}
