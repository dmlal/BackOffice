package com.sparta.backoffice.follow.dto;

import com.sparta.backoffice.follow.entity.Follow;
import com.sparta.backoffice.follow.repository.FollowRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FollowResponseDto {

    private List<String> follower;
    private List<String> following;

    public FollowResponseDto(List<String> follower) {
        this.follower = follower;
    }
}
