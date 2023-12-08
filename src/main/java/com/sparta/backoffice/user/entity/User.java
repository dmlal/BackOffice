package com.sparta.backoffice.user.entity;


import com.sparta.backoffice.follow.entity.Follow;
import com.sparta.backoffice.global.entity.BaseEntity;
import com.sparta.backoffice.like.entity.Like;
import com.sparta.backoffice.post.entity.Post;
import com.sparta.backoffice.user.constant.UserRoleEnum;

import com.sparta.backoffice.user.dto.request.ProfileUpdateRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Table(name = "users")
@RequiredArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String nickname;

    @Column(name = "introduction")
    private String intro;

    @Column(name = "profile_link")
    private String link;

    @Column(name = "profile_image")
    private String profileImageUrl;

    @Column(name = "is_private")
    private Boolean isPrivate;

    @Column(name = "kakao_id")
    private Long kakaoId;

    @Column(name = "naver_id")
    private Long naverId;

    @OneToMany(mappedBy = "user")
    private List<Post> postList;
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

      @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PasswordHistory> passwordHistories = new ArrayList<>();

@OneToMany(mappedBy = "following")  // 팔로잉을 찾으면 follower를 불러온다
    private List<com.sparta.backoffice.follow.entity.Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "follower")   // following
    private List<Follow> followings = new ArrayList<>();


    public User updateProfile(ProfileUpdateRequestDto requestDto) {
        this.nickname = requestDto.getNickname();
        this.intro = requestDto.getIntro();
        this.link = requestDto.getLink();
        this.isPrivate = requestDto.getIsPrivate();

        return this;
    }

    public User updatePassword(String password) {
        this.password = password;

        return this;
    }

    public User(String username, String password, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // 팔로우와 다대 1

    public void addLike(Like like) {
        likes.add(like);
    }
}
