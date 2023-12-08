package com.sparta.backoffice.follow.service;

import com.sparta.backoffice.follow.dto.FollowUserResponseDto;
import com.sparta.backoffice.follow.entity.Follow;
import com.sparta.backoffice.follow.repository.FollowRepository;
import com.sparta.backoffice.global.exception.ApiException;
import com.sparta.backoffice.user.entity.User;
import com.sparta.backoffice.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.sparta.backoffice.global.constant.ErrorCode.*;

@Service
public class FollowService {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void followUser(Long toFollowUserId, User authUser) {
        User toFollowUser = foundUser(toFollowUserId);

        if (toFollowUserId.equals(authUser.getId())) {
            throw new ApiException(CAN_NOT_FOLLOW_YOURSELF);
        }

        Optional<Follow> getFollow = followRepository.findByFollowerIdAndFollowingId(authUser.getId(), toFollowUser.getId());

        if (getFollow.isPresent()) {
            throw new ApiException(ALREADY_FOLLOW_USER);
        }

        Follow follow = new Follow(authUser, toFollowUser);
        followRepository.save(follow);
    }

    @Transactional
    public void unfollowUser(Long toUnfollowUserId, User authUser) {
        User toUnfollowUser = foundUser(toUnfollowUserId);

        if (toUnfollowUserId.equals(authUser.getId())) {
            throw new ApiException(CAN_NOT_UNFOLLOW_YOURSELF);
        }

        Optional<Follow> getUnfollow = followRepository.findByFollowerIdAndFollowingId(authUser.getId(), toUnfollowUser.getId());

        if (getUnfollow.isEmpty()) {
            throw new ApiException(ALREADY_UNFOLLOW_USER);
        }
        followRepository.delete(getUnfollow.get());
    }

    public List<FollowUserResponseDto> getFollowerList(Long userId, User user) {
        User getFollowListInfo = foundUser(userId);

        List<Follow> followingList = followRepository.findAllByFollowerId(userId);
        return followingList.stream().map(follow -> new FollowUserResponseDto(follow.getFollowing())).toList();

    }

    public List<FollowUserResponseDto> getFollowingList(Long userId, User user) {
        User getFollowListInfo = foundUser(userId);

        List<Follow> followerList = followRepository.findAllByFollowingId(userId);
        return followerList.stream().map(follow -> new FollowUserResponseDto(follow.getFollowing())).toList();
    }

    private User foundUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ApiException(NOT_FOUND_USER_ERROR));
    }
}