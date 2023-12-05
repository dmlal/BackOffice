package com.sparta.backoffice.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostRequestDto {
    @NotBlank(message = "내용을 입력해 주세요!")
    String content;
    Long parentPostId;
}
