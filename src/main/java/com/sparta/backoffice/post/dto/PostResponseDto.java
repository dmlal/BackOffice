package com.sparta.backoffice.post.dto;

import com.sparta.backoffice.post.entity.Post;
import com.sparta.backoffice.post.entity.PostImage;
import com.sparta.backoffice.user.dto.UserSimpleDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostResponseDto {
    Long id;
    String content;
    List<String> imageUrls;
    LocalDateTime createdAt;
    LocalDateTime modifiedAt;
    UserSimpleDto userSimpleDto;
    Integer replyCount;
    Long parentPostId;
    Integer likesCount;
    Boolean isDeleted;
    Boolean isPrivate;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.imageUrls = post.getImages().stream().map(PostImage::getImageUrl).toList();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.replyCount = post.getChildPosts().size();
        if (post.getParentPost() != null)
            this.parentPostId = post.getParentPost().getId();
        this.userSimpleDto = new UserSimpleDto(post.getUser());
        this.isDeleted = post.isDeleted();
        this.likesCount = post.getLikes().size();
        this.isPrivate = post.getUser().getIsPrivate();
    }
}
