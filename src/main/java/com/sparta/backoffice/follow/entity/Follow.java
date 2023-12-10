package com.sparta.backoffice.follow.entity;

import com.sparta.backoffice.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // follower_id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_id") // 로그인한 아이디
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_id") // 로그인한 사람한테 팔로우를 받는 아이디
    private User toUser;

    public Follow(User authUser, User toUser) {
        this.fromUser = authUser;
        this.toUser = toUser;
    }
}
