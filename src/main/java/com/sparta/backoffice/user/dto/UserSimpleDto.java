package com.sparta.backoffice.user.dto;

import com.sparta.backoffice.user.entity.User;
import lombok.Getter;

@Getter
public class UserSimpleDto {
    private Long id;
    private String nickname;
    private String username;
    private String profileImageUrl;
    private Boolean isPrivate;

    public UserSimpleDto(User user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.username = user.getUsername();
        this.profileImageUrl = user.getProfileImageUrl();
        this.isPrivate = user.getIsPrivate();
    }

    public UserSimpleDto(String fileUrl) {
        this.profileImageUrl = fileUrl;
    }
}
