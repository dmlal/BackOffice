package com.sparta.backoffice.post.entity;

import com.sparta.backoffice.global.entity.BaseEntity;
import com.sparta.backoffice.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Table(name = "posts")
@NoArgsConstructor
@AllArgsConstructor
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "parent_post_id")
    private Post parentPost;

    @OneToMany(mappedBy = "parentPost")
    private List<Post> childPosts;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
