package com.sparta.backoffice.user.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProfileUpdateRequestDto {
    @Schema(description = "변경할 닉네임", nullable = false, example = "귀요미")
    private final String nickname;
    @Size(max = 140, message = "소개글은 140자 이하로 작성해주세요.")
    @Schema(description = "변경할 소개글", nullable = false, example = "안녕하세요 반갑습니다")
    private final String intro;
    @Schema(description = "변경할 링크", nullable = false, example = "www.youtube.com")
    private final String link;
    @Schema(description = "비공개 여부", nullable = false, example = "true")
    private final Boolean isPrivate;
}
