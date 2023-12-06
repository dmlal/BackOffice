package com.sparta.backoffice.like.repository;

import com.sparta.backoffice.like.entity.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findLikeByUserIdAndPostId(Long userId, Long postId);

    Page<Like> findAllByPostId(Long postId, Pageable pageable);

    Boolean existsLikeByUserIdAndPostId(Long userId, Long postId);
}
