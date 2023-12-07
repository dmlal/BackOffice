package com.sparta.backoffice.post.dto;

import com.sparta.backoffice.post.entity.Post;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Getter
public class PostDetailsResponseDto {
    private List<PostResponseDto> parents;
    private PostResponseDto currentPost;
    private List<ChildPostResponseDto> childs;


    public PostDetailsResponseDto(Post post) {
        Post current = post;
        parents = new ArrayList<>();
        Stack<PostResponseDto> stack = new Stack<>();
        while (current.getParentPost() != null) {
            stack.push(new PostResponseDto(current.getParentPost()));
            current = current.getParentPost();
        }
        while (!stack.isEmpty())
            parents.add(stack.pop());
        currentPost = new PostResponseDto(post);
        childs = post.getChildPosts().stream().map(ChildPostResponseDto::new).toList();

    }
}
