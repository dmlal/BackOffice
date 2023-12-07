package com.sparta.backoffice.post.repository;

import com.sparta.backoffice.post.entity.Post;
import com.sparta.backoffice.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByUserAndParentPostIsNullAndIsDeletedFalse(User user, Pageable pageable);
    Optional<Post> findByIdAndIsDeletedFalse(Long id);
}
