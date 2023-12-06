package com.sparta.backoffice.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDetailsRequestDto {
    private String username;

    public UserDetailsRequestDto(String username) {
        this.username = username;
    }
}
