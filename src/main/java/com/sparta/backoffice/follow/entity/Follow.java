package com.sparta.backoffice.follow.entity;

import com.sparta.backoffice.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follower_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name ="user_Id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "following_id")
    private User followingId;
}
