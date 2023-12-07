package com.sparta.backoffice.follow.service;

import com.sparta.backoffice.follow.dto.FollowRequestDto;
import com.sparta.backoffice.follow.dto.FollowResponseDto;
import com.sparta.backoffice.follow.entity.Follow;
import com.sparta.backoffice.follow.entity.FollowID;
import com.sparta.backoffice.follow.repository.FollowRepository;
import com.sparta.backoffice.global.exception.ApiException;
import com.sparta.backoffice.user.entity.User;
import com.sparta.backoffice.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.sparta.backoffice.global.constant.ErrorCode.DENIED_AUTHORITY;
import static com.sparta.backoffice.global.constant.ErrorCode.NOT_FOUND_USER;

@Service
public class FollowService {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;


    public void followUser(Long toFollowUserId, User authUser) {
        User toFolloewUser = foundUser(toFollowUserId);

        if (toFollowUserId.equals(authUser.getId())) {
            throw new IllegalArgumentException("나 자신 팔로우 금지");  // 있다가 에러코드로 수정
        }

        Optional<Follow> follow = followRepository.findById()

        FollowID followID = new FollowID(authUser.getId(), followingUser.getId());
        followRepository.findById(followID);
        Follow follow = new Follow(followID);

        followRepository.save(follow);
    }

    private User foundUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ApiException(NOT_FOUND_USER));
    }

}
