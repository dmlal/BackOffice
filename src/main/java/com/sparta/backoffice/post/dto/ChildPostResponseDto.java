package com.sparta.backoffice.post.dto;

import com.sparta.backoffice.post.entity.Post;
import com.sparta.backoffice.user.dto.UserSimpleDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ChildPostResponseDto {
    Long id;
    String content;
    LocalDateTime createdAt;
    LocalDateTime modifiedAt;
    UserSimpleDto userSimpleDto;
    Integer replyCount;
    Long parentPostId;
    Integer likesCount;
    Boolean isDeleted;
    Boolean isPrivate;
    List<ChildPostResponseDto> childs;

    public ChildPostResponseDto(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.replyCount = post.getChildPosts().size();
        if (post.getParentPost() != null)
            this.parentPostId = post.getParentPost().getId();
        this.userSimpleDto = new UserSimpleDto(post.getUser());
        this.likesCount = post.getLikes().size();
        this.isDeleted = post.isDeleted();
        this.childs = post.getChildPosts().stream().map(ChildPostResponseDto::new).toList();
        this.isPrivate = post.getUser().getIsPrivate();
    }
}
