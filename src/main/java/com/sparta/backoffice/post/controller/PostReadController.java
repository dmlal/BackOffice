package com.sparta.backoffice.post.controller;

import com.sparta.backoffice.global.constant.ResponseCode;
import com.sparta.backoffice.global.dto.BaseResponse;
import com.sparta.backoffice.like.dto.LikeUserResponseDto;
import com.sparta.backoffice.post.dto.PostResponseDto;
import com.sparta.backoffice.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.backoffice.global.constant.ResponseCode.GET_USER_POSTS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostReadController {

    private final PostService postService;

    //유저별 게시글 목록 조회
    @GetMapping("/users/{userId}/posts")
    public ResponseEntity<BaseResponse<List<PostResponseDto>>> getPostsByUser(
            @PathVariable Long userId,
            @RequestParam Integer cursor,
            @RequestParam Integer size,
            @RequestParam String dir
    ) {
        List<PostResponseDto> postsByUser = postService.getPostsByUser(userId, cursor, size, dir);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.of(
                        userId + GET_USER_POSTS.getMessage(),
                        GET_USER_POSTS.getHttpStatus(),
                         postsByUser));
    }
}
