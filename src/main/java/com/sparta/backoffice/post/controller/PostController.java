package com.sparta.backoffice.post.controller;

import com.sparta.backoffice.global.annotation.AuthUser;
import com.sparta.backoffice.global.dto.BaseResponse;
import com.sparta.backoffice.post.dto.PostRequestDto;
import com.sparta.backoffice.post.dto.PostResponseDto;
import com.sparta.backoffice.post.service.PostService;
import com.sparta.backoffice.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.sparta.backoffice.global.constant.ResponseCode.*;

@Tag(name = "Post 컨트롤러", description = "Post API 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    @Operation(summary = "게시글 작성 API", description = "사용자가 게시글을 작성하거나 다른 게시글에 답글을 작성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "게시글 작성 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "403", description = "내가 팔로잉하지 않은 비공개 게시물에는 답글 작성", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글에 답글 작성", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "내용이 공백이거나 140자 초과일 때", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping
    public ResponseEntity<BaseResponse<PostResponseDto>> createPost(
            @RequestParam("images") MultipartFile[] images,
            @RequestPart("data") @Valid PostRequestDto requestDto,
            @AuthUser User user
    ) {
        PostResponseDto postResponseDto = postService.createPost(requestDto, user, images);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                BaseResponse.of(CREATED_POST, postResponseDto));
    }

    @Operation(summary = "게시글 수정 API", description = "사용자가 게시글을 수정합니다.",
            parameters = {@Parameter(name = "postId", description = "수정할 게시글 번호", in = ParameterIn.PATH)})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글 수정", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "작성자가 아닌 사용자가 수정", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "내용이 공백이거나 140자 초과일 때", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PutMapping("/{postId}")
    public ResponseEntity<BaseResponse<PostResponseDto>> updatePost(@PathVariable Long postId, @RequestBody @Valid PostRequestDto requestDto, @AuthUser User user) {
        PostResponseDto postResponseDto = postService.updatePost(requestDto, postId, user);
        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.of(MODIFIED_POST, postResponseDto));
    }

    @Operation(summary = "게시글 삭제 API", description = "사용자가 게시글을 삭제합니다.",
            parameters = {@Parameter(name = "postId", description = "삭제할 게시글 번호", in = ParameterIn.PATH)}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 삭제 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글 삭제", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "작성자가 아닌 사용자가 삭제", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @DeleteMapping("/{postId}")
    public ResponseEntity<BaseResponse<String>> deletePost(@PathVariable Long postId, @AuthUser User user) {
        postService.deletePost(postId, user);
        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.of(DELETED_POST, ""));
    }


}
