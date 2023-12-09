package com.sparta.backoffice.user.dto.request;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.File;

@Getter
@RequiredArgsConstructor
public class ProfileUpdateRequestDto {

    private final String nickname;

    private final String intro;

    private final String link;

    private final Boolean isPrivate;
}
