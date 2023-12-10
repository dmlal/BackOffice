package com.sparta.backoffice.user.controller;

import com.sparta.backoffice.global.annotation.AuthUser;
import com.sparta.backoffice.global.constant.ResponseCode;
import com.sparta.backoffice.global.dto.BaseResponse;
import com.sparta.backoffice.user.dto.UserInfoDto;
import com.sparta.backoffice.user.entity.User;
import com.sparta.backoffice.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "유저 관리 컨트롤러", description = "유저 관리 컨트롤러 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/users")
public class AdminUserConroller {
    private final UserService userService;

    @Operation(summary = "모든 유저 목록 조회", description = "모든 유저 목록 조회 API",
            parameters = {
                    @Parameter(name = "cursor", description = "페이지 번호", in = ParameterIn.QUERY),
                    @Parameter(name = "size", description = "페이지에 표시할 게시물 개수", in = ParameterIn.QUERY),
                    @Parameter(name = "dir", description = "정렬 방향", in = ParameterIn.QUERY)
            })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "모든 유저 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "권한이 없습니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            )
    })
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

    @Operation(summary = "회원 강제 탈퇴 API", description = "관리자가 사용자을 강제로 탈퇴 시킵니다.",
            parameters = {@Parameter(name = "postId", description = "탈퇴 시킬 사용자 번호", in = ParameterIn.PATH)}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 강퇴 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "관리자는 탈퇴 시킬 수 없습니다.", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    //회원 강제 탈퇴
    @DeleteMapping("/{userId}")
    public ResponseEntity<BaseResponse<String>> deleteUser(@PathVariable Long userId, @AuthUser User user) {
        userService.deleteUser(userId, user);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.of(ResponseCode.DELETED_USER, ""));
    }


    @Operation(summary = "회원 차단 API", description = "관리자가 사용자 권한을 차단시킵니다.",
            parameters = {@Parameter(name = "userId", description = "차단시킬 사용자 번호", in = ParameterIn.PATH)}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 차단 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "이미 차단한 사용자 입니다.", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "관리자는 차단 시킬 수 없습니다.", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    //회원 강제 차단
    @PutMapping("/{userId}/block")
    public ResponseEntity<BaseResponse<String>> blockUser(@PathVariable Long userId) {
        userService.blockUser(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.of(ResponseCode.BLOCK_USER, ""));
    }

    @Operation(summary = "회원 차단 해지 API", description = "관리자가 사용자 차단을 해지시킵니다.",
            parameters = {@Parameter(name = "userId", description = "차단 해지시킬 사용자 번호", in = ParameterIn.PATH)}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 차단 해지 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "차단한 적 없는 사용자 입니다.", content = @Content(schema = @Schema(implementation = BaseResponse.class))), @ApiResponse(responseCode = "400", description = "관리자는 차단 시킬 수 없습니다.", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    //회원 차단 해지
    @PutMapping("/{userId}/unblock")
    public ResponseEntity<BaseResponse<String>> unblockUser(@PathVariable Long userId) {
        userService.unblockUser(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.of(ResponseCode.UNBLOCK_USER, ""));
    }
}
