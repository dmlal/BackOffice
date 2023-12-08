package com.sparta.backoffice.post.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sparta.backoffice.global.constant.ErrorCode;
import com.sparta.backoffice.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;
    private final String POST_PATH_PREFIX = "posts/";

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    public List<String> uploadPostImages(String postId, MultipartFile[] images) {
        List<String> result = new ArrayList<>();
        try {
            for (int i = 0; i < images.length; i++) {
                MultipartFile image = images[i];
                String filename = image.getOriginalFilename();
                String filenameExtension = StringUtils.getFilenameExtension(filename);
                filename = POST_PATH_PREFIX + postId + "/" + UUID.randomUUID().toString() + "." + filenameExtension;
                String fileUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + filename;

                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(image.getContentType());
                metadata.setContentLength(image.getSize());
                PutObjectRequest putObjectRequest =
                        new PutObjectRequest(bucket, filename, image.getInputStream(), metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead);
                amazonS3Client.putObject(putObjectRequest);
                result.add(fileUrl);
            }
            return result;
        } catch (IOException e) {
            throw new ApiException(ErrorCode.CAN_NOT_READ_IMAGE);
        }
    }

    public String uploadProfileImage(String userId, MultipartFile image) {
        try {
            String filename = image.getOriginalFilename();
            String filenameExtension = StringUtils.getFilenameExtension(filename);
            filename = "profile-image/" + userId + "." + filenameExtension;
            String fileUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + filename;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(image.getContentType());
            metadata.setContentLength(image.getSize());
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, filename, image.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3Client.putObject(putObjectRequest);
            return fileUrl;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteProfileImage(String filename) {
        amazonS3Client.deleteObject(bucket, "profile-image/" + filename);
    }
}
