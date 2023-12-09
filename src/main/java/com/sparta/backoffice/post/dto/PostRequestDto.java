package com.sparta.backoffice.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostRequestDto {
    @NotBlank(message = "내용을 입력해 주세요!")
    @Size(min = 1, max = 140, message = "내용은 1자 이상 140자 이하로 입력해 주세요")
    @Schema(description = "게시글 내용", nullable = false, example = "오늘 날씨 참 좋다!")
    String content;
    @Schema(description = "답글 쓸 게시글 번호", nullable = true, example = "1")
    Long parentPostId;
}
