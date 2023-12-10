package com.sparta.backoffice.profileImage.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;


    public String uploadFile(MultipartFile file, String fileName) {
        File fileObj = convertMultiPartFileToFile(file);
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, fileObj));
        fileObj.delete();

        String fileUrl = amazonS3Client.getUrl(bucket, fileName).toString();
        return fileUrl;
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("파일변환 실패 : ", e);
        }
        return convertedFile;
    }

    public void deleteFile(String fileName) {
        amazonS3Client.deleteObject(bucket, fileName);
    }
}
