package com.sparta.backoffice.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginRequest {
	@Schema(description = "로그인 아이디", example = "username12")
	@Pattern(
		regexp = "^[a-z0-9]+$",
		message = "아이디는 알파벳 소문자, 숫자의 조합으로 입력해야합니다."
	)
	@Size(
		min = 4, max = 10,
		message = "아이디는 4자리 이상 10자리 이하로 입력해야합니다."
	)
	@NotBlank
	private final String username;

	@Schema(description = "로그인 비밀번호", example = "password12")
	@Pattern(
		regexp = "^^(?=.*[a-zA-Z])(?=.*\\\\d)(?=.*[@#$%^&+=!]).*$",
		message = "비밀번호는 알파벳 대/소문자, 숫자, 특수문자의 조합으로 입력해야합니다."
	)
	@Size(
		min = 8,
		max = 15,
		message = "비밀번호는 8자리 이상, 15자리 이하로 입력해야합니다."
	)
	@NotBlank
	private final String password;
}
