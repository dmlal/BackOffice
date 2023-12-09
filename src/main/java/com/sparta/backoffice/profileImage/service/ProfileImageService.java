package com.sparta.backoffice.profileImage.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sparta.backoffice.user.entity.User;
import com.sparta.backoffice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileImageService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketname;

    private final UserService userService;

    private final S3Uploader s3Uploader;

    public String uploadFile(MultipartFile file, Long userId, User authUser) {
        User requestUser = userService.foundUser(userId);
        userService.checkUserPermission(requestUser, authUser);

        String fileName = Long.toString(userId);
        return s3Uploader.uploadFile(file, fileName);
    }



    public void deleteFile(Long userId, User authUser) {
        User requestUser = userService.foundUser(userId);
        userService.checkUserPermission(requestUser, authUser);

        String fileName = Long.toString(userId);
        s3Uploader.deleteFile(fileName);
    }
}
