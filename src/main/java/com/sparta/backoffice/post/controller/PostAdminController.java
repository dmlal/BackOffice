package com.sparta.backoffice.post.controller;

import com.sparta.backoffice.global.annotation.AuthUser;
import com.sparta.backoffice.global.dto.BaseResponse;
import com.sparta.backoffice.post.dto.PostRequestDto;
import com.sparta.backoffice.post.dto.PostResponseDto;
import com.sparta.backoffice.post.service.PostService;
import com.sparta.backoffice.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.sparta.backoffice.global.constant.ResponseCode.DELETED_POST;
import static com.sparta.backoffice.global.constant.ResponseCode.MODIFIED_POST;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/posts")
public class PostAdminController {
    private final PostService postService;

    @PutMapping("/{postId}")
    public ResponseEntity<BaseResponse<String>> updatePost(@PathVariable Long postId, @RequestBody @Valid PostRequestDto requestDto, @AuthUser User user) {
        PostResponseDto postResponseDto = postService.updatePost(requestDto, postId, user);
        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.of(MODIFIED_POST, ""));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<BaseResponse<String>> deletePost(@PathVariable Long postId, @AuthUser User user) {
        postService.deletePost(postId, user);
        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.of(DELETED_POST, ""));
    }
}
