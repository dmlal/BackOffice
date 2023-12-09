package com.sparta.backoffice.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@RequiredArgsConstructor
public class PasswordUpdateRequestDto {

    // 기존 패스워드라서 이미 정규식 통과.
    private final String password;


    @Pattern(
            regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-])[a-zA-Z0-9@#$%^&+=!]*$",
            message = "비밀번호는 영어 대/소문자, 숫자, 특수문자의 조합으로 입력해야합니다."
    )
    @Schema(description = "변경할 비밀번호", nullable = false, example = "!!User1111")
    private final String newPassword;
}


