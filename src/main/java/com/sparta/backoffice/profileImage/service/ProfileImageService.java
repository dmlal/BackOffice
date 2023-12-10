package com.sparta.backoffice.profileImage.service;

import com.sparta.backoffice.user.entity.User;
import com.sparta.backoffice.user.repository.UserRepository;
import com.sparta.backoffice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileImageService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketname;

    private final UserService userService;

    private final UserRepository userRepository;

    private final S3Uploader s3Uploader;

    public String uploadFile(MultipartFile file, Long userId, User authUser) {
        User requestUser = userService.foundUser(userId);
        userService.checkUserPermission(requestUser, authUser);

        String fileName = Long.toString(userId);
        String fileUrl =  s3Uploader.uploadFile(file, fileName);

        requestUser.setProfileImageUrl(fileUrl);
        userRepository.save(requestUser);

        return fileUrl;
    }



    public void deleteFile(Long userId, User authUser) {
        User requestUser = userService.foundUser(userId);
        userService.checkUserPermission(requestUser, authUser);

        String fileName = Long.toString(userId);
        s3Uploader.deleteFile(fileName);

        requestUser.setProfileImageUrl(null);
        userRepository.save(requestUser);
    }
}
