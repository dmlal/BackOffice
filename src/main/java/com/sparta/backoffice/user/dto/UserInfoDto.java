package com.sparta.backoffice.user.dto;

import com.sparta.backoffice.user.constant.UserRoleEnum;
import com.sparta.backoffice.user.entity.User;
import lombok.Getter;

@Getter
public class UserInfoDto {
    private Long id;
    private String username;
    private String nickname;
    private String intro;
    private String link;
    private String profileImageUrl;
    private Boolean isPrivate;
    private String kakaoId;
    private String naverId;
    private Integer postCount;
    private UserRoleEnum role;
    private Integer follower;
    private Integer following;

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
        this.follower = user.getFollowers().size();
        this.following = user.getFollowings().size();
    }
}
