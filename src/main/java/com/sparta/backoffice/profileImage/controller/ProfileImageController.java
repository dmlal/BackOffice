package com.sparta.backoffice.profileImage.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.sparta.backoffice.global.annotation.AuthUser;
import com.sparta.backoffice.global.dto.BaseResponse;
import com.sparta.backoffice.profileImage.service.ProfileImageService;
import com.sparta.backoffice.user.dto.UserSimpleDto;
import com.sparta.backoffice.user.entity.User;
import com.sparta.backoffice.user.service.UserService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

import static com.sparta.backoffice.global.constant.ResponseCode.UPDATE_PROFILE_IMAGE;

@RestController
@RequestMapping("/api/users/userImage")
@RequiredArgsConstructor
public class ProfileImageController {

    private final ProfileImageService profileImageService;

    @PostMapping("/{userId}")
    public ResponseEntity<BaseResponse<UserSimpleDto>> changeProfileImage(@PathVariable("userId") Long userId,
                                   @RequestParam(value = "file") MultipartFile file,
                                   @AuthUser User authUser) {

        String fileUrl = profileImageService.uploadFile(file, userId, authUser);
        UserSimpleDto userSimpleDto = new UserSimpleDto(fileUrl);

        return ResponseEntity
                .status(UPDATE_PROFILE_IMAGE.getHttpStatus())
                .body(BaseResponse.of(UPDATE_PROFILE_IMAGE, userSimpleDto));
    }

    @DeleteMapping("/{userId}")
    public void deleteProfileImage(@PathVariable("userId") Long userId, @AuthUser User authUser) {

        profileImageService.deleteFile(userId, authUser);
    }


//    @GetMapping("/{userId}")
//    public ResponseEntity<BaseResponse<UserSimpleDto>> getUser(@PathVariable("userId") Long userId, @AuthUser User user) {
//        User requestUser = userService.foundUser(userId);
//        userService.checkUserPermission(requestUser, user);
//
//        URL url = amazonS3Client.getUrl("x-snsbucket", Long.toString(userId));
//        String urltext = "" + url;
//        프로필이미지레포.save(urltext);
//
//        UserSimpleDto userSimpleDto = new UserSimpleDto(requestUser);
//
//
//        return ResponseEntity
//                .status(UPDATE_PROFILE_IMAGE.getHttpStatus())
//                .body(BaseResponse.of(UPDATE_PROFILE_IMAGE, userSimpleDto));
//    }       주석은 내일 테스트 완전히 끝나면 지우겠습니다.

}
