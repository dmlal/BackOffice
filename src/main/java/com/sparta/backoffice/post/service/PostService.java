package com.sparta.backoffice.post.service;

import com.sparta.backoffice.global.exception.ApiException;
import com.sparta.backoffice.post.dto.PostRequestDto;
import com.sparta.backoffice.post.dto.PostResponseDto;
import com.sparta.backoffice.post.entity.Post;
import com.sparta.backoffice.post.repository.PostRepository;
import com.sparta.backoffice.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.backoffice.global.constant.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public PostResponseDto createPost(PostRequestDto requestDto, User user) {
        Post savedPost;

        if (requestDto.getParentPostId() != null) {
            //부모가 존재
            Post parentPost = postRepository.findById(requestDto.getParentPostId()).orElseThrow(
                    () -> new ApiException(CAN_NOT_REPLY_POST_ERROR));

            Post post = new Post(requestDto, parentPost, user);
            savedPost = postRepository.save(post);
        } else {
            //부모가 존재하지 않음
            Post post = new Post(requestDto, user);
            savedPost = postRepository.save(post);
        }

        return new PostResponseDto(savedPost);
    }

    @Transactional
    public PostResponseDto updatePost(PostRequestDto requestDto, Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ApiException(NOT_FOUND_POST_ERROR));

        if (post.getUser().getId() != user.getId()) {
            throw new ApiException(CAN_NOT_MODIFY_ERROR);
        }

        //내용 수정
        post.update(requestDto);

        return new PostResponseDto(post);
    }

    public void deletePost(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ApiException(NOT_FOUND_POST_ERROR));

        if (post.getUser().getId() != user.getId()) {
            throw new ApiException(CAN_NOT_DELETE_ERROR);
        }

        // 일단 연관 관게를 끊는 방식으로 구혐
        if (!post.getChildPosts().isEmpty()) {
            post.removeChilds();
        }

        postRepository.delete(post);
    }

}
