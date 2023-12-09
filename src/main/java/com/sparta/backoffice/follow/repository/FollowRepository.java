package com.sparta.backoffice.follow.repository;

import com.sparta.backoffice.follow.entity.Follow;
import com.sparta.backoffice.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFromUserAndToUser(User FromUser, User ToUser);
}
