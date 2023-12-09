package com.sparta.backoffice.user.controller;


import com.sparta.backoffice.global.annotation.AuthUser;
import com.sparta.backoffice.global.constant.ResponseCode;
import com.sparta.backoffice.global.dto.BaseResponse;
import com.sparta.backoffice.user.dto.UserInfoDto;
import com.sparta.backoffice.user.entity.User;
import com.sparta.backoffice.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/read/users")
public class UserReadController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<BaseResponse<UserInfoDto>> getUserInfo(@PathVariable Long userId, @AuthUser User user) {
        UserInfoDto userInfo = userService.getUserInfo(userId, user);
        return ResponseEntity.status(ResponseCode.GET_ALL_USER.getHttpStatus())
                .body(
                        BaseResponse.of(
                                ResponseCode.GET_USER
                                , userInfo
                        )
                );
    }


}