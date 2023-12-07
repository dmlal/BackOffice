package com.sparta.backoffice.post.repository;

import com.sparta.backoffice.post.entity.Post;
import com.sparta.backoffice.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p where p.isDeleted = false and p.id in " +
            "(select p2.id from Like l join l.post p2 where l.user.id = :userId and (p2.user.isPrivate = false " +
            "or (p2.user.isPrivate = true and p2.user.id in " +
            "(select f.following.id from Follow f where f.follower.id = :userId))))")
    Page<Post> findPostsByLikes(Long userId, Pageable pageable);

    @Query("select p from Post p join p.user u where p.isDeleted = false and (u.id in (" +
            "select f.following.id from Follow f where f.follower.id = :userId) or u.id = :userId)")
    Page<Post> findPostsByFollowingUsers(Long userId, Pageable pageable);

    Page<Post> findByUserAndParentPostIsNullAndIsDeletedFalse(User user, Pageable pageable);

    Optional<Post> findByIdAndIsDeletedFalse(Long id);
}