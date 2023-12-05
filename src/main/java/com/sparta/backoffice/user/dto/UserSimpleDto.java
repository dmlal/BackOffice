package com.sparta.backoffice.user.dto;

import com.sparta.backoffice.user.entity.User;
import lombok.Getter;

@Getter
public class UserSimpleDto {
    private Long id;
    private String username;
    private String profileImageUrl;

    public UserSimpleDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.profileImageUrl = user.getProfileImageUrl();
    }
}
