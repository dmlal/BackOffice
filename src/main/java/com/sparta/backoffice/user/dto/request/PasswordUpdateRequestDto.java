package com.sparta.backoffice.user.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@RequiredArgsConstructor
public class PasswordUpdateRequestDto {

    // 정규식 특수문자는 일단 !만 넣어둠.  후에 회원가입과 비교하여 변경
    @Pattern(regexp = "^[a-zA-Z0-9!]{8,20}$")
    private final String password;


    @Pattern(regexp = "^[a-zA-Z0-9!]{8,20}$")
    private final String newPassword;
}


