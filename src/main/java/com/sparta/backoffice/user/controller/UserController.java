package com.sparta.backoffice.user.controller;

import com.sparta.backoffice.global.dto.BaseResponse;
import com.sparta.backoffice.global.security.CustomUserDetails;
import com.sparta.backoffice.user.dto.request.PasswordUpdateRequestDto;
import com.sparta.backoffice.user.dto.request.ProfileUpdateRequestDto;
import com.sparta.backoffice.user.dto.request.UserDetailsRequestDto;
import com.sparta.backoffice.user.dto.response.PasswordUpdateResponseDto;
import com.sparta.backoffice.user.dto.response.ProfileUpdateResponseDto;
import com.sparta.backoffice.user.entity.User;
import com.sparta.backoffice.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;



    @PutMapping("/{userId}")
    public ResponseEntity<BaseResponse<ProfileUpdateResponseDto>> updateProfile(@PathVariable Long userId,
                                                                                @RequestBody ProfileUpdateRequestDto requestDto,
                                                                                @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserDetailsRequestDto userDetailsRequestDto = new UserDetailsRequestDto();
        userDetailsRequestDto.setUsername(userDetails.getUsername());


        ProfileUpdateResponseDto responseDto = userService.updateProfile(userId, requestDto, userDetailsRequestDto);

        return ResponseEntity
                .ok()
                .body(new BaseResponse<>("수정 완료", HttpStatus.OK.value(), responseDto));
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<BaseResponse<PasswordUpdateResponseDto>> updatePassword(@PathVariable Long userId,
                                                                                  @Valid @RequestBody PasswordUpdateRequestDto requestDto,
                                                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserDetailsRequestDto userDetailsRequestDto = new UserDetailsRequestDto();
        userDetailsRequestDto.setUsername(userDetails.getUsername());

        PasswordUpdateResponseDto responseDto = userService.updatePassword(userId, requestDto, userDetailsRequestDto);

        return ResponseEntity
                .ok()
                .body(new BaseResponse<>("변경 완료", HttpStatus.OK.value(), responseDto));

    }
}






