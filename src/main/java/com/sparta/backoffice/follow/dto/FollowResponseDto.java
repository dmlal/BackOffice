package com.sparta.backoffice.follow.dto;

import com.sparta.backoffice.follow.entity.Follow;
import com.sparta.backoffice.follow.repository.FollowRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class FollowResponseDto {

    private Long followerId;
    private Long followingId;

}
