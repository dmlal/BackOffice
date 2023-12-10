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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User 조회 컨트롤러", description = "User 조회 컨트롤러 API")
@RestController
@RequestMapping("/api/read/users")
public class UserReadController {

    @Autowired
    private UserService userService;


    @Operation(summary = "유저별 정보 조회", description = "모든 유저별 정보 조회 API",
            parameters = {
                    @Parameter(name = "userId", description = "조회할 사용자 번호", in = ParameterIn.PATH),
                    @Parameter(name = "cursor", description = "페이지 번호", in = ParameterIn.QUERY),
                    @Parameter(name = "size", description = "페이지에 표시할 게시물 개수", in = ParameterIn.QUERY),
                    @Parameter(name = "dir", description = "정렬 방향", in = ParameterIn.QUERY)
            })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "유저별 정보 조회 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 사용자 입니다.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            )
    })
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