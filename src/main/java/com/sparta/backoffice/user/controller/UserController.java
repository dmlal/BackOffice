package com.sparta.backoffice.user.controller;

import com.sparta.backoffice.global.annotation.AuthUser;
import com.sparta.backoffice.global.dto.BaseResponse;
import com.sparta.backoffice.user.dto.request.PasswordUpdateRequestDto;
import com.sparta.backoffice.user.dto.request.ProfileUpdateRequestDto;
import com.sparta.backoffice.user.dto.response.PasswordUpdateResponseDto;
import com.sparta.backoffice.user.dto.response.ProfileUpdateResponseDto;
import com.sparta.backoffice.user.entity.User;
import com.sparta.backoffice.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;



    @PutMapping("/{userId}")
    public ResponseEntity<BaseResponse<ProfileUpdateResponseDto>> updateProfile(@PathVariable Long userId,
                                                                                @RequestBody ProfileUpdateRequestDto requestDto,
                                                                                @AuthUser User authUser) {


        ProfileUpdateResponseDto responseDto = userService.updateProfile(userId, requestDto, authUser);

        return ResponseEntity
                .ok()
                .body(new BaseResponse<>("수정 완료", HttpStatus.OK.value(), responseDto));
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<BaseResponse<PasswordUpdateResponseDto>> updatePassword(@PathVariable Long userId,
                                                                                  @Valid @RequestBody PasswordUpdateRequestDto requestDto,
                                                                                  @AuthUser User authUser) {

        PasswordUpdateResponseDto responseDto = userService.updatePassword(userId, requestDto, authUser);

        return ResponseEntity
                .ok()
                .body(new BaseResponse<>("변경 완료", HttpStatus.OK.value(), responseDto));

    }
}






