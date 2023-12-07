package com.sparta.backoffice.post.repository;

import com.sparta.backoffice.post.entity.Post;
import com.sparta.backoffice.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p " +
            "where p.id in (select l.post.id from Like l where l.user.id = :userId)")
    Page<Post> findPostsByLikesUserId(Long userId, Pageable pageable);

    @Query("select p from Post p join p.user u" +
            " where u.id in " +
            "(select f.followingId from Follow f where f.followerId = :userId and ) or u.id = :userId")
    Page<Post> findPostsByFollowingUsers(Long userId, Pageable pageable);

    Page<Post> findByUserAndParentPostIsNullAndIsDeletedFalse(User user, Pageable pageable);

    Optional<Post> findByIdAndIsDeletedFalse(Long id);
}