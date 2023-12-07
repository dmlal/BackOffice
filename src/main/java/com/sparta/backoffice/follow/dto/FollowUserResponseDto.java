package com.sparta.backoffice.follow.dto;

import com.sparta.backoffice.user.entity.User;
import lombok.Getter;

@Getter
public class FollowUserResponseDto {
    private final Long id;
    private final String profileImageUrl;
    private final String nickname;

    public FollowUserResponseDto(User user) {
        this.id = user.getId();
        this.profileImageUrl = user.getProfileImageUrl();
        this.nickname = user.getNickname();
    }
}
