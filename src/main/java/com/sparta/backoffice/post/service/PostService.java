package com.sparta.backoffice.post.service;

import com.sparta.backoffice.global.exception.ApiException;
import com.sparta.backoffice.post.dto.PostDetailsResponseDto;
import com.sparta.backoffice.post.dto.PostRequestDto;
import com.sparta.backoffice.post.dto.PostResponseDto;
import com.sparta.backoffice.post.entity.Post;
import com.sparta.backoffice.post.repository.PostRepository;
import com.sparta.backoffice.user.constant.UserRoleEnum;
import com.sparta.backoffice.user.entity.User;
import com.sparta.backoffice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.sparta.backoffice.global.constant.ErrorCode.*;

@Slf4j(topic = "PostService")
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostResponseDto createPost(PostRequestDto requestDto, User user) {
        Post post;

        if (requestDto.getParentPostId() != null) {
            //부모가 존재
            Post parentPost = postRepository.findByIdAndIsDeletedFalse(requestDto.getParentPostId()).orElseThrow(
                    () -> new ApiException(CAN_NOT_REPLY_POST_ERROR));

            post = new Post(requestDto, parentPost, user);

        } else {
            //부모가 존재하지 않음
            post = new Post(requestDto, user);
        }
        Post savedPost = postRepository.save(post);
        return new PostResponseDto(savedPost);
    }

    @Transactional
    public PostResponseDto updatePost(PostRequestDto requestDto, Long postId, User user) {

        Post post = postRepository.findByIdAndIsDeletedFalse(postId).orElseThrow(
                () -> new ApiException(NOT_FOUND_POST_ERROR));

        if (user.getRole() != UserRoleEnum.ADMIN && !post.getUser().getId().equals(user.getId())) {
            throw new ApiException(CAN_NOT_MODIFY_ERROR);
        }

        //내용 수정
        post.update(requestDto);

        return new PostResponseDto(post);
    }


    @Transactional
    public void deletePost(Long postId, User user) {
        List<Long> tobedeletedList = new ArrayList<>();//삭제 예정 리스트 초기화

        Post post = postRepository.findByIdAndIsDeletedFalse(postId).orElseThrow(
                () -> new ApiException(NOT_FOUND_POST_ERROR));

        if (user.getRole() != UserRoleEnum.ADMIN && !post.getUser().getId().equals(user.getId())) {
            throw new ApiException(CAN_NOT_DELETE_ERROR);
        }

        post.changeStateIsDeleted();//상태 변경
        log.info(post.getId() + "번 게시물 삭제 상태로 변경 되었습니다");

        // 자식이 없을 떄
        if (post.getChildPosts().isEmpty()) {
            // 자기 자신을 삭제 예정 리스트에 추가
            tobedeletedList.add(post.getId());

            Post currentPost = post;

            //부모가 존재 한다면
            while (currentPost.getParentPost() != null) {
                Long parentPostId = currentPost.getParentPost().getId();
                Post parentPost = postRepository.findById(parentPostId).orElseThrow(
                );

                //부모가 삭제 상태 면서
                if (parentPost.isDeleted() &&
                        isAllChildsDeleted(parentPost.getChildPosts())) {
                    tobedeletedList.add(parentPost.getId());//부모를 삭제 예정 리스트에 추가
                } else {
                    break;//부모가 삭제 상태 아니거나 활성화된 자식이 존재하면 종료
                }
                currentPost = parentPost;//부모의 부모가 있는지 찾기위해 현재 게시글에 부모를 대입
            }

            //자식부터 부모까지 순차적으로 삭제한다.
            for (Long deletedId : tobedeletedList) {
                postRepository.deleteById(deletedId);
            }
        }
    }

    private boolean isAllChildsDeleted(List<Post> childPosts) {
        for (Post post : childPosts) {
            if (!post.isDeleted() || !isAllChildsDeleted(post.getChildPosts())) {
                log.info(post.getId() + "번 게시물 활성화 상태입니다");
                return false;
            }
        }
        return true;
    }


    @Transactional(readOnly = true)
    public List<PostResponseDto> getPostsByUser(Long userId, Integer cursor, Integer size, String dir) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ApiException(NOT_FOUND_USER_ERROR)
        );

        Sort sort = Sort.by(dir.equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC, "createdAt");

        Pageable pageable = PageRequest.of(cursor, size, sort);

        Page<Post> posts = postRepository.findByUserAndParentPostIsNullAndIsDeletedFalse(user, pageable);

        return posts.stream().map(PostResponseDto::new).toList();
    }

    public PostDetailsResponseDto getPost(Long postId) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId).orElseThrow(
                () -> new ApiException(NOT_FOUND_POST_ERROR));

        return new PostDetailsResponseDto(post);
    }
}
