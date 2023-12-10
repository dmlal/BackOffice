package com.sparta.backoffice.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.List;

@Getter
public class PostUpdateDto {

    @NotBlank(message = "내용을 입력해 주세요!")
    private String content;

    private List<String> deleteFileUrls;
}
