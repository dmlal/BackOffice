package com.sparta.backoffice.profileImage.controller;

import com.sparta.backoffice.global.annotation.AuthUser;
import com.sparta.backoffice.global.dto.BaseResponse;
import com.sparta.backoffice.profileImage.service.ProfileImageService;
import com.sparta.backoffice.user.dto.UserSimpleDto;
import com.sparta.backoffice.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.sparta.backoffice.global.constant.ResponseCode.DELETE_PROFILE_IMAGE;
import static com.sparta.backoffice.global.constant.ResponseCode.UPDATE_PROFILE_IMAGE;

@RestController
@RequestMapping("/api/users/userImage")
@RequiredArgsConstructor
public class ProfileImageController {

    private final ProfileImageService profileImageService;

    @Operation(summary = "프로필 이미지 수정", description = "프로필 이미지 수정 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "수정 완료",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "유저를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "권한이 없습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            )
    })
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
    @Operation(summary = "프로필 이미지 삭제", description = "프로필 이미지 삭제 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "삭제 완료",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "유저를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "권한이 없습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            )
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<BaseResponse<String>> deleteProfileImage(@PathVariable("userId") Long userId, @AuthUser User authUser) {

        profileImageService.deleteFile(userId, authUser);

        return ResponseEntity
                .status(DELETE_PROFILE_IMAGE.getHttpStatus())
                .body(BaseResponse.of(DELETE_PROFILE_IMAGE,""));
    }
}
