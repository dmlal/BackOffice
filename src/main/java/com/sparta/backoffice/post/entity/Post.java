package com.sparta.backoffice.post.entity;

import com.sparta.backoffice.global.entity.BaseEntity;
import com.sparta.backoffice.post.dto.PostRequestDto;
import com.sparta.backoffice.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
    private List<Post> childPosts = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private boolean isDeleted = false;

    public Post(PostRequestDto requestDto, Post parentPost, User user) {
        this.content = requestDto.getContent();
        this.parentPost = parentPost;
        this.user = user;
    }

    public Post(PostRequestDto requestDto, User user) {
        this.content = requestDto.getContent();
        this.user = user;
    }

    public void update(PostRequestDto requestDto) {
        this.content = requestDto.getContent();
    }

    public void changeStateIsDeleted() {
        this.isDeleted = true;
    }
}
