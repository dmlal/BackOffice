package com.sparta.backoffice.follow.controller;

import com.sparta.backoffice.follow.dto.FollowUserResponseDto;
import com.sparta.backoffice.follow.service.FollowService;
import com.sparta.backoffice.global.annotation.AuthUser;
import com.sparta.backoffice.global.dto.BaseResponse;
import com.sparta.backoffice.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.backoffice.global.constant.ResponseCode.*;

@RestController
@RequestMapping("/api/follows")
public class FollowController {

    @Autowired
    private FollowService followService;


    @Operation(summary = "유저 팔로우", description = "유저 팔로우 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "팔로우 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 사용자입니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "자기자신은 팔로우 할 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            ),
            @ApiResponse(
            responseCode = "400",
            description = "이미 팔로우한 사용자입니다.",
            content = @Content(schema = @Schema(implementation = BaseResponse.class))
            )
    })
    @PostMapping("/{userId}")
    public ResponseEntity<BaseResponse<String>> followUser(@PathVariable Long userId, @AuthUser User authUser) {

        followService.followUser(userId, authUser);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.of(FOLLOW_USER,""));
    }

    @Operation(summary = "유저 언팔로우", description = "유저 언팔로우 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "언팔로우",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 사용자입니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "자기자신은 언팔로우 할 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "이미 언팔로우한 사용자입니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            )
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<BaseResponse<String>> unfollowUser(@PathVariable Long userId, @AuthUser User user) {

        followService.unfollowUser(userId, user);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.of(UNFOLLOW_USER,""));
    }

    @Operation(summary = "팔로워리스트", description = "유저 팔로우리스트 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "팔로워 리스트",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 사용자입니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            )
    })
    @GetMapping("/{userId}/follower")
    public ResponseEntity<BaseResponse<List<FollowUserResponseDto>>> getFollowerList(@PathVariable Long userId, @AuthUser User user) {
        List<FollowUserResponseDto> responseDto = followService.getFollowerList(userId, user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseResponse.of(GET_FOLLOW_LIST, responseDto));
    }

    @Operation(summary = "팔로잉 리스트", description = "유저 팔로잉 리스트 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "팔로잉 리스트",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 사용자입니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            )
    })
    @GetMapping("/{userId}/follower")
    public ResponseEntity<BaseResponse<List<FollowUserResponseDto>>> getFollowingList(@PathVariable Long userId, @AuthUser User user) {
        List<FollowUserResponseDto> responseDto = followService.getFollowingList(userId, user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseResponse.of(GET_FOLLOW_LIST, responseDto));

    }

}
