package com.sparta.backoffice.post.controller;

import com.sparta.backoffice.global.annotation.AuthUser;
import com.sparta.backoffice.global.dto.BaseResponse;
import com.sparta.backoffice.post.dto.PostResponseDto;
import com.sparta.backoffice.post.service.PostService;
import com.sparta.backoffice.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.backoffice.global.constant.ResponseCode.*;

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

    @GetMapping("/users/{userId}/likes/posts")
    public ResponseEntity<BaseResponse<List<PostResponseDto>>> getUserLikedPosts(
            @PathVariable Long userId,
            @RequestParam Integer cursor,
            @RequestParam Integer size,
            @RequestParam String dir
    ) {
        List<PostResponseDto> userLikedPosts = postService.getUserLikedPosts(userId, cursor, size, dir);
        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.of(GET_LIKE_POSTS, userLikedPosts));
    }

    @GetMapping("/follows/posts")
    public ResponseEntity<BaseResponse<List<PostResponseDto>>> getFollowingPosts(
            @RequestParam Integer cursor,
            @RequestParam Integer size,
            @RequestParam String dir,
            @AuthUser User user
    ) {
        List<PostResponseDto> followingPosts = postService.getFollowingPosts(cursor, size, dir, user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.of(GET_FOLLOWING_POSTS, followingPosts));
    }
}
