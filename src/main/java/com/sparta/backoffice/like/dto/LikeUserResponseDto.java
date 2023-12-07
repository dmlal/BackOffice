package com.sparta.backoffice.like.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LikeUserResponseDto {

    private String nickname;
    private String intro;
    private String profileImageUrl;
}
