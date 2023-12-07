package com.sparta.backoffice.user.controller;

import com.sparta.backoffice.global.annotation.AuthUser;
import com.sparta.backoffice.global.dto.BaseResponse;
import com.sparta.backoffice.user.dto.request.PasswordUpdateRequestDto;
import com.sparta.backoffice.user.dto.request.ProfileUpdateRequestDto;
import com.sparta.backoffice.user.dto.response.ProfileUpdateResponseDto;
import com.sparta.backoffice.user.entity.User;
import com.sparta.backoffice.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.sparta.backoffice.global.constant.ResponseCode.UPDATE_PASSWORD;
import static com.sparta.backoffice.global.constant.ResponseCode.UPDATE_PROFILE;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;



@Operation(summary = "프로필 수정", description = "프로필 수정 API")
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
        ),
        @ApiResponse(
                responseCode = "400",
                description = "닉네임을 변경할 수 없습니다.",
                content = @Content(schema = @Schema(implementation = BaseResponse.class))
        )
})

    @PutMapping("/{userId}")
    public ResponseEntity<BaseResponse<ProfileUpdateResponseDto>> updateProfile(@PathVariable Long userId,
                                                                                @RequestBody ProfileUpdateRequestDto requestDto,
                                                                                @AuthUser User authUser) {


        ProfileUpdateResponseDto responseDto = userService.updateProfile(userId, requestDto, authUser);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.of(UPDATE_PROFILE, responseDto));
    }

    @Operation(summary = "비밀번호 수정", description = "비밀번호 수정 API")
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
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "최근 사용한 비밀번호입니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            )
    })
    @PutMapping("/{userId}/password")
    public ResponseEntity<BaseResponse<String>> updatePassword(@PathVariable Long userId,
                                                                                  @Valid @RequestBody PasswordUpdateRequestDto requestDto,
                                                                                  @AuthUser User authUser) {

        userService.updatePassword(userId, requestDto, authUser);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.of(UPDATE_PASSWORD, ""));

    }
}






