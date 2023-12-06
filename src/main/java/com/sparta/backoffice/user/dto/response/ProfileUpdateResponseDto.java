package com.sparta.backoffice.user.dto.response;

import com.sparta.backoffice.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateResponseDto {

    private String nickname;

    private String intro;

    private String link;

    private Boolean isPrivate;

    public ProfileUpdateResponseDto(User newProfile) {
        this.nickname = newProfile.getNickname();
        this.intro = newProfile.getIntro();
        this.link = newProfile.getIntro();
        this.isPrivate = newProfile.getIsPrivate();
    }
}
