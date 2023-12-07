package com.sparta.backoffice.like.controller;

import com.sparta.backoffice.global.annotation.AuthUser;
import com.sparta.backoffice.global.constant.ResponseCode;
import com.sparta.backoffice.global.dto.BaseResponse;
import com.sparta.backoffice.like.dto.LikeUserResponseDto;
import com.sparta.backoffice.like.service.LikeService;
import com.sparta.backoffice.user.dto.UserSimpleDto;
import com.sparta.backoffice.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/likes/{postId}")
    public ResponseEntity<BaseResponse<Void>> like(
            @PathVariable Long postId,
            @AuthUser User user
    ) {
        likeService.like(user, postId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.of(ResponseCode.CREATED_LIKE, null));
    }

    @DeleteMapping("/likes/{postId}")
    public ResponseEntity<BaseResponse<Void>> unlike(
            @PathVariable Long postId,
            @AuthUser User user
    ) {
        likeService.unlike(user, postId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.of(ResponseCode.DELETED_LIKE, null));
    }

    @GetMapping("/likes/{postId}")
    public ResponseEntity<BaseResponse<List<UserSimpleDto>>> getLikedUsers(
            @PathVariable Long postId,
            @RequestParam Integer cursor,
            @RequestParam Integer size,
            @RequestParam String dir
    ) {
        List<UserSimpleDto> likedUsers = likeService.getLikedUsers(postId, cursor, size, dir);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.of(ResponseCode.GET_LIKE_USERS, likedUsers));
    }
}
