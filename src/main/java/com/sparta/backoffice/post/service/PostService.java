package com.sparta.backoffice.post.service;

import com.sparta.backoffice.global.constant.ErrorCode;
import com.sparta.backoffice.post.dto.PostRequestDto;
import com.sparta.backoffice.post.dto.PostResponseDto;
import com.sparta.backoffice.post.entity.Post;
import com.sparta.backoffice.post.repository.PostRepository;
import com.sparta.backoffice.user.entity.User;
import com.sparta.backoffice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostResponseDto createPost(PostRequestDto requestDto, Long userId) {
        //임시로 user 가져오는 코드 -> 삭제 예정
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NullPointerException(ErrorCode.NOT_FOUND_USER_ERROR.getMessage())
        );

        Post savedPost;

        if (requestDto.getParentPostId() != null) {
            //부모가 존재
            Post parentPost = postRepository.findById(requestDto.getParentPostId()).orElseThrow(
                    () -> new NullPointerException("존재 하지 않는 게시글에 답글을 달 수 없습니다.")
            );
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
    public PostResponseDto updatePost(PostRequestDto requestDto, Long postId, Long userId) {
        //임시로 user 가져오는 코드 -> 삭제 예정
        User loginUser = userRepository.findById(userId).orElseThrow(
                () -> new NullPointerException(ErrorCode.NOT_FOUND_USER_ERROR.getMessage())
        );

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new NullPointerException(ErrorCode.NOT_FOUND_POST_ERROR.getMessage())
        );

        if (post.getUser().getId() != loginUser.getId()) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        //내용 수정
        post.update(requestDto);

        return new PostResponseDto(post);
    }

    public void deletePost(Long postId, Long userId) {
        //임시로 user 가져오는 코드 -> 삭제 예정
        User loginUser = userRepository.findById(userId).orElseThrow(
                () -> new NullPointerException(ErrorCode.NOT_FOUND_USER_ERROR.getMessage())
        );

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new NullPointerException(ErrorCode.NOT_FOUND_POST_ERROR.getMessage())
        );

        if (post.getUser().getId() != loginUser.getId()) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        // 일단 연관 관게를 끊는 방식으로 구혐
        if (!post.getChildPosts().isEmpty()) {
            post.removeChilds();
        }

        postRepository.delete(post);
    }

}
