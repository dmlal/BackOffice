package com.sparta.backoffice.post.controller;

import com.sparta.backoffice.global.annotation.AuthUser;
import com.sparta.backoffice.global.dto.BaseResponse;
import com.sparta.backoffice.post.dto.PostDetailsResponseDto;
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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.backoffice.global.constant.ResponseCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/read")
public class PostReadController {

    private final PostService postService;

    @Operation(summary = "게시글 상세 조회", description = "해당 게시글 번호의 부모 게시글들과 답글들을 모두 볼 수 있습니다",
            parameters = {@Parameter(name = "postId", description = "조회할 게시글 번호", in = ParameterIn.PATH)})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "로그인한 사람이 존재하지 않는 사용자", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글 조회", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    //게시글 상세 조회
    @GetMapping("/posts/{postId}")
    public ResponseEntity<BaseResponse<PostDetailsResponseDto>> getPost(@PathVariable Long postId, @AuthUser User user) {
        PostDetailsResponseDto responseDto = postService.getPost(postId, user);
        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.of(GET_POST_DETAIL, responseDto));
    }

    @Operation(summary = "유저별 게시글 조회", description = "유저의 루트 게시글들(답글이 아닌 글)을 모두 볼 수 있습니다",
            parameters = {@Parameter(name = "userId", description = "조회할 유저 번호", in = ParameterIn.PATH),
                    @Parameter(name = "cursor", description = "페이지 번호", in = ParameterIn.QUERY),
                    @Parameter(name = "size", description = "페이지에 표시할 게시물 개수", in = ParameterIn.QUERY),
                    @Parameter(name = "dir", description = "정렬 방향", in = ParameterIn.QUERY)
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저별 게시글 조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "비공개 계정의 게시글 조회", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저 조회", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    //유저별 게시글 목록 조회
    @GetMapping("/users/{userId}/posts")
    public ResponseEntity<BaseResponse<List<PostResponseDto>>> getPostsByUser(
            @PathVariable Long userId,
            @RequestParam Integer cursor,
            @RequestParam Integer size,
            @RequestParam String dir,
            @AuthUser User loginUser
    ) {
        List<PostResponseDto> postsByUser = postService.getPostsByUser(userId, cursor, size, dir, loginUser);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.of(
                        userId + GET_USER_POSTS.getMessage(),
                        GET_USER_POSTS.getHttpStatus(),
                        postsByUser));
    }

    @Operation(summary = "유저별 좋아요한 게시글 조회", description = "유저가 좋아요한 게시글을 모두 볼 수 있습니다",
            parameters = {@Parameter(name = "userId", description = "조회할 유저 번호", in = ParameterIn.PATH),
                    @Parameter(name = "cursor", description = "페이지 번호", in = ParameterIn.QUERY),
                    @Parameter(name = "size", description = "페이지에 표시할 게시물 개수", in = ParameterIn.QUERY),
                    @Parameter(name = "dir", description = "정렬 방향", in = ParameterIn.QUERY)
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저별 좋아요한 게시글 조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "비공개 계정이 좋아요한 게시글 조회", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저 조회", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    //유저별 좋아요한 게시글 조회
    @GetMapping("/users/{userId}/likes/posts")
    public ResponseEntity<BaseResponse<List<PostResponseDto>>> getUserLikedPosts(
            @PathVariable Long userId,
            @RequestParam Integer cursor,
            @RequestParam Integer size,
            @RequestParam String dir,
            @AuthUser User loginUser
    ) {
        List<PostResponseDto> userLikedPosts = postService.getUserLikedPosts(userId, cursor, size, dir, loginUser);
        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.of(GET_LIKE_POSTS, userLikedPosts));
    }


    @Operation(summary = "팔로잉한 게시글 조회", description = "유저와 유저가 팔로잉한 계정들의 게시글을 모두 볼 수 있습니다",
            parameters = {@Parameter(name = "userId", description = "조회할 유저 번호", in = ParameterIn.PATH),
                    @Parameter(name = "cursor", description = "페이지 번호", in = ParameterIn.QUERY),
                    @Parameter(name = "size", description = "페이지에 표시할 게시물 개수", in = ParameterIn.QUERY),
                    @Parameter(name = "dir", description = "정렬 방향", in = ParameterIn.QUERY)
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팔로잉한 게시글 조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
    })
    //팔로잉한 게시글 조회
    @GetMapping("/follows/posts")
    public ResponseEntity<BaseResponse<List<PostResponseDto>>> getFollowingPosts(
            @RequestParam Integer cursor,
            @RequestParam Integer size,
            @RequestParam String dir,
            @AuthUser User user
    ) {
        List<PostResponseDto> followingPosts = postService.getFollowingPosts(cursor, size, dir, user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.of(GET_FOLLOWING_POSTS, followingPosts));
    }
}
