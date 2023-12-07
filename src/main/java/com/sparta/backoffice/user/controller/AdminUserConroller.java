package com.sparta.backoffice.user.controller;

import com.sparta.backoffice.global.annotation.AuthUser;
import com.sparta.backoffice.global.constant.ResponseCode;
import com.sparta.backoffice.global.dto.BaseResponse;
import com.sparta.backoffice.user.dto.UserInfoDto;
import com.sparta.backoffice.user.dto.request.ProfileUpdateRequestDto;
import com.sparta.backoffice.user.dto.response.ProfileUpdateResponseDto;
import com.sparta.backoffice.user.entity.User;
import com.sparta.backoffice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.backoffice.global.constant.ResponseCode.UPDATE_PROFILE;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/users")
public class AdminUserConroller {
    private final UserService userService;

    //모든 유저 목록 조회
    @GetMapping
    public ResponseEntity<BaseResponse<List<UserInfoDto>>> getAllUsers(
            @RequestParam Integer cursor,
            @RequestParam Integer size,
            @RequestParam String dir
    ) {

        List<UserInfoDto> allUsers = userService.getAllUsers(cursor, size, dir);
        return ResponseEntity.ok().body(
                BaseResponse.of(
                        ResponseCode.GET_ALL_USER,
                        allUsers
                )
        );
    }

    //모든 회원 정보 수정 가능
    @PutMapping("/{userId}")
    public ResponseEntity<BaseResponse<ProfileUpdateResponseDto>> updateProfile(@PathVariable Long userId,
                                                                                @RequestBody ProfileUpdateRequestDto requestDto,
                                                                                @AuthUser User authUser) {


        ProfileUpdateResponseDto responseDto = userService.updateProfile(userId, requestDto, authUser);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.of(UPDATE_PROFILE, responseDto));
    }

    //유저 회원 탈퇴
    @DeleteMapping("/{userId}")
    public ResponseEntity<BaseResponse<String>> deleteUser(@PathVariable Long userId, @AuthUser User user) {
        userService.deleteUser(userId, user);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.of(ResponseCode.DELETED_USER,""));
    }

    //유저 권한 변경
    //유저 차단
}
