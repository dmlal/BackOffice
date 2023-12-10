package com.sparta.backoffice.post.repository;

import com.sparta.backoffice.post.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    Optional<PostImage> findPostImageByImageUrl(String imageUrl);
}