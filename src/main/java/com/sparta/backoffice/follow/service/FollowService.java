package com.sparta.backoffice.follow.service;

import com.sparta.backoffice.follow.dto.FollowUserResponseDto;
import com.sparta.backoffice.follow.entity.Follow;
import com.sparta.backoffice.follow.repository.FollowRepository;
import com.sparta.backoffice.global.exception.ApiException;
import com.sparta.backoffice.user.constant.UserRoleEnum;
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

        Optional<Follow> getFollow = followRepository.findByFromUserAndToUser(authUser, toFollowUser);

        if (getFollow.isPresent()) {
            throw new ApiException(ALREADY_FOLLOW_USER);
        }

        if (!authUser.getRole().equals(UserRoleEnum.ADMIN)) {
            if (toFollowUser.getIsPrivate()) {
                throw new ApiException(CAN_NOT_FOLLOW_PRIVATE);
            }
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

        Optional<Follow> getUnfollow = followRepository.findByFromUserAndToUser(authUser, toUnfollowUser);

        if (getUnfollow.isEmpty()) {
            throw new ApiException(ALREADY_UNFOLLOW_USER);
        }

        followRepository.delete(getUnfollow.get());
    }

    public List<FollowUserResponseDto> getFollowerList(Long userId, User authUser) {
        User findUser = foundUser(userId);//to일 떄(팔로우를 받는 사람)

        validateFollowing(findUser, authUser);

        return findUser.getFollowers().stream().map(
                follow -> {
                    return new FollowUserResponseDto(follow.getFromUser());
                }).toList();

    }

    public List<FollowUserResponseDto> getFollowingList(Long userId, User authUser) {
        User findUser = foundUser(userId);//from일 떄(팔로우를 하는 사람)

        validateFollowing(findUser, authUser);

        return findUser.getFollowings().stream().map(
                follow -> {
                    return new FollowUserResponseDto(follow.getToUser());
                }).toList();

    }

    private User foundUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ApiException(NOT_FOUND_USER_ERROR));
    }

    void validateFollowing(User findUser, User authUser) {
        if (!authUser.getRole().equals(UserRoleEnum.ADMIN)) {
            if (findUser.getIsPrivate() && findUser.getId().equals(authUser.getId())) {
                followRepository.findByFromUserAndToUser(authUser, findUser).orElseThrow(
                        () -> new ApiException(IS_PRIVATE_USER)
                );
            }
        }
    }

}