package com.sparta.backoffice.follow.controller;

import com.sparta.backoffice.follow.dto.FollowRequestDto;
import com.sparta.backoffice.follow.dto.FollowResponseDto;
import com.sparta.backoffice.follow.service.FollowService;
import com.sparta.backoffice.global.annotation.AuthUser;
import com.sparta.backoffice.global.dto.BaseResponse;
import com.sparta.backoffice.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follows")
public class FollowController {

    @Autowired
    private FollowService followService;

    @PostMapping("/{userId}")
    public ResponseEntity<BaseResponse<String>> followUser(@PathVariable Long userId, @AuthUser User authUser){

        followService.followUser(userId, authUser);

        return ResponseEntity
                .ok()
                .body(new BaseResponse<>("얘를 팔로우함!", HttpStatus.OK.value(), ""));
    }

//    @DeleteMapping("/{userId}")
//    public ResponseEntity<BaseResponse<String>> deleteFollowUser(@PathVariable Long userId,
//                                                                            @RequestBody FollowRequestDto requestDto,
//                                                                            @AuthUser User user) {
//
//        followService.deleteFollowUser(userId, requestDto, user);
//
//        return ResponseEntity
//                .ok()
//                .body(new BaseResponse<>("팔로우 취소", HttpStatus.OK.value(), ""));
//    }

}
