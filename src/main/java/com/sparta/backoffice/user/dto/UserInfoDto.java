package com.sparta.backoffice.user.dto;

import com.sparta.backoffice.post.entity.Post;
import com.sparta.backoffice.user.constant.UserRoleEnum;
import com.sparta.backoffice.user.entity.PasswordHistory;
import com.sparta.backoffice.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UserInfoDto {
    private Long id;
    private String username;
    private String nickname;
    private String intro;
    private String link;
    private String profileImageUrl;
    private Boolean isPrivate;
    private Long kakaoId;
    private Long naverId;
    private Integer postCount;
    private UserRoleEnum role;

    public UserInfoDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.intro = user.getIntro();
        this.link = user.getLink();
        this.profileImageUrl = user.getProfileImageUrl();
        this.isPrivate = user.getIsPrivate();
        this.kakaoId = user.getKakaoId();
        this.naverId = user.getNaverId();
        this.postCount = user.getPostList().size();
        this.role = user.getRole();
    }
}
