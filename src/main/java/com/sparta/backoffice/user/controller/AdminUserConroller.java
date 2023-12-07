package com.sparta.backoffice.user.controller;

import com.sparta.backoffice.global.constant.ResponseCode;
import com.sparta.backoffice.global.dto.BaseResponse;
import com.sparta.backoffice.user.dto.UserInfoDto;
import com.sparta.backoffice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


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
    //유저 회원 탈퇴
    //유저 권한 변경
    //유저 차단
}
