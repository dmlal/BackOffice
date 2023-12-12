package com.sparta.backoffice.post.service;

import com.sparta.backoffice.global.constant.ErrorCode;
import com.sparta.backoffice.global.exception.ApiException;
import com.sparta.backoffice.post.entity.Post;
import com.sparta.backoffice.post.entity.PostImage;
import com.sparta.backoffice.post.repository.PostImageRepository;
import com.sparta.backoffice.post.s3.S3Manager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostImageService {

    private final PostImageRepository postImageRepository;
    private final S3Manager s3Manager;
    public final static String POST_PATH_PREFIX = "posts/";

    @Transactional(propagation = Propagation.MANDATORY)
    public void uploadImages(Post post, MultipartFile[] images) throws Exception {
        for (MultipartFile image : images) {
            if (image.getContentType() == null) {
                throw new RuntimeException();
            }
            String imageUrl = s3Manager.uploadMultipartFileWithPublicRead(
                    POST_PATH_PREFIX + post.getId().toString() + "/",
                    image
            );


            PostImage postImage = new PostImage(post, imageUrl);
            postImageRepository.save(postImage);
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteImage(Post post, String fileUrl) {
        s3Manager.deletePostFile(fileUrl);
        for (PostImage image : post.getImages()) {
            if (image.getImageUrl().equals(fileUrl)) {
                post.getImages().remove(image);
                postImageRepository.delete(image);
                break;
            }
        }
    }

    // 게시글의 모든 파일 삭제
    public void deleteAll(Long postId) {
        s3Manager.deleteAllPostFiles(postId.toString());
    }
}
