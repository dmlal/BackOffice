package com.sparta.backoffice.follow.repository;

import com.sparta.backoffice.follow.entity.Follow;
import com.sparta.backoffice.follow.entity.FollowID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, FollowID> {
}
