package com.sparta.backoffice.user.dto.response;

import com.sparta.backoffice.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PasswordUpdateResponseDto {
    private Long userId;

    private String username;

    public PasswordUpdateResponseDto(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
    }

}
