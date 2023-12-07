package com.sparta.backoffice.post.controller;

import com.sparta.backoffice.global.annotation.AuthUser;
import com.sparta.backoffice.global.dto.BaseResponse;
import com.sparta.backoffice.post.dto.PostDetailsResponseDto;
import com.sparta.backoffice.post.dto.PostRequestDto;
import com.sparta.backoffice.post.dto.PostResponseDto;
import com.sparta.backoffice.post.service.PostService;
import com.sparta.backoffice.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.sparta.backoffice.global.constant.ResponseCode.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<BaseResponse<PostResponseDto>> createPost(@RequestBody @Valid PostRequestDto requestDto, @AuthUser User user) {
        PostResponseDto postResponseDto = postService.createPost(requestDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                BaseResponse.of(CREATED_POST, postResponseDto));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<BaseResponse<PostResponseDto>> updatePost(@PathVariable Long postId, @RequestBody @Valid PostRequestDto requestDto, @AuthUser User user) {
        PostResponseDto postResponseDto = postService.updatePost(requestDto, postId, user);
        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.of(MODIFIED_POST, postResponseDto));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<BaseResponse<Object>> updatePost(@PathVariable Long postId, @AuthUser User user) {
        postService.deletePost(postId, user);
        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.of(DELETED_POST, null));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<BaseResponse<PostDetailsResponseDto>> getPost(@PathVariable Long postId) {
        PostDetailsResponseDto responseDto = postService.getPost(postId);
        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.of("게시글 상세 조회 성공", HttpStatus.OK.value(), responseDto));
    }
}
