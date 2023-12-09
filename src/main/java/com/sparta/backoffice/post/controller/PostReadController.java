package com.sparta.backoffice.post.controller;

import com.sparta.backoffice.global.annotation.AuthUser;
import com.sparta.backoffice.global.dto.BaseResponse;
import com.sparta.backoffice.post.dto.PostDetailsResponseDto;
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
@RequestMapping("/api/read")
public class PostReadController {

    private final PostService postService;

    //게시글 상세 조회
    @GetMapping("/posts/{postId}")
    public ResponseEntity<BaseResponse<PostDetailsResponseDto>> getPost(@PathVariable Long postId, @AuthUser User user) {
        PostDetailsResponseDto responseDto = postService.getPost(postId,user);
        return ResponseEntity.status(HttpStatus.OK).body(
            BaseResponse.of(GET_POST_DETAIL, responseDto));
    }

    //유저별 게시글 목록 조회
    @GetMapping("/users/{userId}/posts")
    public ResponseEntity<BaseResponse<List<PostResponseDto>>> getPostsByUser(
        @PathVariable Long userId,
        @RequestParam Integer cursor,
        @RequestParam Integer size,
        @RequestParam String dir,
        @AuthUser User loginUser
    ) {
        List<PostResponseDto> postsByUser = postService.getPostsByUser(userId, cursor, size, dir,loginUser);

        return ResponseEntity.status(HttpStatus.OK)
            .body(BaseResponse.of(
                userId + GET_USER_POSTS.getMessage(),
                GET_USER_POSTS.getHttpStatus(),
                postsByUser));
    }

    //유저별 좋아요한 게시글 조회
    @GetMapping("/users/{userId}/likes/posts")
    public ResponseEntity<BaseResponse<List<PostResponseDto>>> getUserLikedPosts(
        @PathVariable Long userId,
        @RequestParam Integer cursor,
        @RequestParam Integer size,
        @RequestParam String dir,
        @AuthUser User loginUser
    ) {
        List<PostResponseDto> userLikedPosts = postService.getUserLikedPosts(userId, cursor, size, dir,loginUser);
        return ResponseEntity.status(HttpStatus.OK)
            .body(BaseResponse.of(GET_LIKE_POSTS, userLikedPosts));
    }

    //팔로잉한 게시글 조회
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
