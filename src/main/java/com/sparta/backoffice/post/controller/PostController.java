package com.sparta.backoffice.post.controller;

import com.sparta.backoffice.global.dto.BaseResponse;
import com.sparta.backoffice.post.dto.PostRequestDto;
import com.sparta.backoffice.post.dto.PostResponseDto;
import com.sparta.backoffice.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<BaseResponse<PostResponseDto>> createPost(@RequestBody @Valid PostRequestDto requestDto, Long id) {
        PostResponseDto postResponseDto = postService.createPost(requestDto, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new BaseResponse<>(
                        "게시글 작성 성공",
                        HttpStatus.CREATED.value(),
                        postResponseDto
                )
        );
    }

    @PutMapping("/{postId}")
    public ResponseEntity<BaseResponse<PostResponseDto>> updatePost(@PathVariable Long postId, @RequestBody @Valid PostRequestDto requestDto, Long id) {
        PostResponseDto postResponseDto = postService.updatePost(requestDto, postId, id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new BaseResponse<>(
                        "게시글 수정 성공",
                        HttpStatus.OK.value(),
                        postResponseDto
                )
        );
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<BaseResponse<Object>> updatePost(@PathVariable Long postId, Long id) {
        postService.deletePost(postId, id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new BaseResponse<>(
                        "게시글 삭제 성공",
                        HttpStatus.OK.value(),
                        null
                )
        );
    }

}
