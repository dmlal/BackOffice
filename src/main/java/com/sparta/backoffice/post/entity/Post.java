package com.sparta.backoffice.post.entity;

import com.sparta.backoffice.global.entity.BaseEntity;
import com.sparta.backoffice.like.entity.Like;
import com.sparta.backoffice.post.dto.PostRequestDto;
import com.sparta.backoffice.post.dto.PostUpdateDto;
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

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<PostImage> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_post_id")
    private Post parentPost;

    @OneToMany(mappedBy = "parentPost")
    private List<Post> childPosts = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "post",cascade = CascadeType.REMOVE)
    private List<Like> likes = new ArrayList<>();

    public Post(PostRequestDto requestDto, Post parentPost, User user) {
        this.content = requestDto.getContent();
        this.parentPost = parentPost;
        this.user = user;
    }

    public Post(PostRequestDto requestDto, User user) {
        this.content = requestDto.getContent();
        this.user = user;
    }

    public void update(PostUpdateDto requestDto) {
        this.content = requestDto.getContent();
    }

    public void changeStateIsDeleted() {
        this.content = "이 게시물은 작성자에 의해 삭제되었습니다.";
        this.isDeleted = true;
    }
}
