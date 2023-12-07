package com.sparta.backoffice.like.service;

import com.sparta.backoffice.global.constant.ErrorCode;
import com.sparta.backoffice.global.exception.ApiException;
import com.sparta.backoffice.like.dto.LikeUserResponseDto;
import com.sparta.backoffice.like.repository.LikeRepository;
import com.sparta.backoffice.like.entity.Like;
import com.sparta.backoffice.post.entity.Post;
import com.sparta.backoffice.post.repository.PostRepository;
import com.sparta.backoffice.user.dto.UserSimpleDto;
import com.sparta.backoffice.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    @Transactional
    public void like(User user, Long postId) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_POST_ERROR));
        // 자기 글에 좋아요 못함
        if (Objects.equals(post.getUser().getId(), user.getId())) {
            throw new ApiException(ErrorCode.SELF_LIKE_ERROR);
        }
        // 이미 좋아요 했으면 좋아요 못함
        if (likeRepository.existsLikeByUserIdAndPostId(user.getId(), postId)) {
            throw new ApiException(ErrorCode.ALREADY_LIKED_ERROR);
        }

        Like like = new Like(user, post);
        likeRepository.save(like);
    }

    @Transactional
    public void unlike(User user, Long postId) {
        // 좋아요 안 했으면 취소 못함
        Like like = likeRepository.findLikeByUserIdAndPostId(user.getId(), postId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_LIKE_ERROR));

        likeRepository.delete(like);
    }

    @Transactional(readOnly = true)
    public List<UserSimpleDto> getLikedUsers(
            Long postId,
            Integer cursor,
            Integer size,
            String direction
    ) {
        Sort sort = Sort.by(direction.equalsIgnoreCase("desc") ?
                Direction.DESC : Direction.ASC, "createdAt");

        Pageable pageable = PageRequest.of(cursor, size, sort);
        Page<Like> likes = likeRepository.findAllByPostId(postId, pageable);

        List<UserSimpleDto> likedUsers = likes.stream().map((like) -> {
            User user = like.getUser();
            return new UserSimpleDto(user);
        }).toList();

        return likedUsers;
    }
}
