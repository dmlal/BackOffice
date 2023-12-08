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
    @JoinColumn(name ="follower_id") // user_id
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id") // following_id
    private User following;

    public Follow(User authUser, User toFolloewUser) {
        this.follower = authUser;
        this.following = toFolloewUser;
    }
}
