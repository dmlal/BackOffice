package com.sparta.backoffice.auth.controller;

import static com.sparta.backoffice.global.constant.ResponseCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.backoffice.auth.dto.request.LoginRequest;
import com.sparta.backoffice.auth.dto.request.SignupRequest;
import com.sparta.backoffice.auth.service.AuthService;
import com.sparta.backoffice.global.dto.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
	public ResponseEntity<BaseResponse<String>> signup(@Valid @RequestBody SignupRequest request) {
		authService.signup(request);
		return ResponseEntity.status(SIGNUP.getHttpStatus())
			.body(
				BaseResponse.of(
					SIGNUP,
					""
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
	public ResponseEntity<BaseResponse<String>> login(@Valid @RequestBody LoginRequest request,
		HttpServletResponse response) {
		authService.login(request, response);

		return ResponseEntity.status(LOGIN.getHttpStatus())
			.body(
				BaseResponse.of(
					LOGIN,
					""
				)
			);
	}

	@Operation(summary = "토큰 재발급",
		description = "토큰 재발급 API",
		parameters = {@Parameter(name = "RefreshToken", description = "리프레쉬 토큰", in = ParameterIn.COOKIE)})
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "201",
			description = "재발급 성공",
			headers = {
				@Header(name = "Authorization", description = "엑세스 토큰", required = true),
				@Header(
					name = "Set-Cookie",
					description = "RefreshToken",
					schema =
					@Schema(type = "String", name = "RefreshToken", description = "리프레쉬 토큰")
				)
			}
		),
		@ApiResponse(
			responseCode = "400",
			description = "재발급 실패 - 유효하지 않은 토큰으로 요청 시 ",
			content = @Content(schema = @Schema(implementation = BaseResponse.class))
		),
	})
	@PostMapping("/reissue")
	public ResponseEntity<BaseResponse<String>> reissue(HttpServletRequest request, HttpServletResponse response) {
		authService.reissue(request, response);
		return ResponseEntity.status(REISSUE_TOKEN.getHttpStatus())
			.body(
				BaseResponse.of(
					REISSUE_TOKEN,
					""
				)
			);
	}

	@Operation(
		summary = "로그아웃",
		description = "로그아웃 API",
		parameters = {
			@Parameter(name = "Authorization", description = "Jwt AccessToken", in = ParameterIn.HEADER),
			@Parameter(name = "RefreshToken", description = "Jwt RefreshToken", in = ParameterIn.COOKIE)
		}
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "204",
			description = "로그아웃 성공"
		),
		@ApiResponse(
			responseCode = "400",
			description = "유효하지 않은 토큰으로 로그아웃 요청한 경우",
			content = @Content(schema = @Schema(implementation = BaseResponse.class))
		)
	})
	@DeleteMapping("/logout")
	public ResponseEntity<BaseResponse<String>> logout(HttpServletRequest request) {
		authService.logout(request);
		return ResponseEntity.status(LOGOUT.getHttpStatus())
			.body(
				BaseResponse.of(
					LOGOUT,
					""
				)
			);
	}

}
