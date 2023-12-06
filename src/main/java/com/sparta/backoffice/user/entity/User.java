package com.sparta.backoffice.user.entity;


import com.sparta.backoffice.global.entity.BaseEntity;
import com.sparta.backoffice.post.entity.Post;
import com.sparta.backoffice.user.constant.UserRoleEnum;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor
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

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserRoleEnum getRole() {
        return role;
    }

    // 좋아요와 1대다

    // 팔로우와 다대 1
}
