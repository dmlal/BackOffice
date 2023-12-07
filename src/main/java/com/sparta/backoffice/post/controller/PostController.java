package com.sparta.backoffice.post.controller;

import com.sparta.backoffice.global.annotation.AuthUser;
import com.sparta.backoffice.global.dto.BaseResponse;
import com.sparta.backoffice.post.dto.PostDetailsResponseDto;
import com.sparta.backoffice.post.dto.PostRequestDto;
import com.sparta.backoffice.post.dto.PostResponseDto;
import com.sparta.backoffice.post.service.PostService;
import com.sparta.backoffice.user.constant.UserRoleEnum;
import com.sparta.backoffice.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static com.sparta.backoffice.global.constant.ResponseCode.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    @Secured(value = {UserRoleEnum.Authority.USER, UserRoleEnum.Authority.ADMIN})
    @PostMapping
    public ResponseEntity<BaseResponse<PostResponseDto>> createPost(@RequestBody @Valid PostRequestDto requestDto, @AuthUser User user) {
        PostResponseDto postResponseDto = postService.createPost(requestDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                BaseResponse.of(CREATED_POST, postResponseDto));
    }

    @Secured(value = {UserRoleEnum.Authority.USER, UserRoleEnum.Authority.ADMIN})
    @PutMapping("/{postId}")
    public ResponseEntity<BaseResponse<PostResponseDto>> updatePost(@PathVariable Long postId, @RequestBody @Valid PostRequestDto requestDto, @AuthUser User user) {
        PostResponseDto postResponseDto = postService.updatePost(requestDto, postId, user);
        String str = UserRoleEnum.ADMIN.getAuthority();
        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.of(MODIFIED_POST, postResponseDto));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<BaseResponse<Object>> updatePost(@PathVariable Long postId, @AuthUser User user) {
        postService.deletePost(postId, user);
        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.of(DELETED_POST, null));
    }

    //내가 팔로잉한 사람이 아니라면 볼 수 없게 처리해야한다.
    @GetMapping("/{postId}")
    public ResponseEntity<BaseResponse<PostDetailsResponseDto>> getPost(@PathVariable Long postId) {
        PostDetailsResponseDto responseDto = postService.getPost(postId);
        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.of(GET_POST_DETAIL, responseDto));
    }
}
