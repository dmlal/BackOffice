package com.sparta.backoffice.post.dto;

import com.sparta.backoffice.post.entity.Post;
import com.sparta.backoffice.user.entity.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Getter
public class PostDetailsResponseDto {
    private List<PostResponseDto> parents;
    private PostResponseDto currentPost;
    private List<ChildPostResponseDto> childs;


    public PostDetailsResponseDto(Post post, User loginUser, List<Long> followingIdList) {
        Post current = post;
        parents = new ArrayList<>();
        Stack<PostResponseDto> stack = new Stack<>();
        while (current.getParentPost() != null) {
            stack.push(new PostResponseDto(current.getParentPost(), loginUser, followingIdList));
            current = current.getParentPost();
        }
        while (!stack.isEmpty())
            parents.add(stack.pop());
        currentPost = new PostResponseDto(post, loginUser, followingIdList);
        childs = post.getChildPosts().stream().map(
                (post1) -> new ChildPostResponseDto(post1,loginUser,followingIdList)
        ).toList();

    }
}
