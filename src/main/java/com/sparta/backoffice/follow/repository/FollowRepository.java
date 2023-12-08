package com.sparta.backoffice.follow.repository;

import com.sparta.backoffice.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerIdAndFollowingId(Long follower, Long following);

    List<Follow> findAllByFollowingId(Long followingId);
    List<Follow> findAllByFollowerId(Long followerId);
}
