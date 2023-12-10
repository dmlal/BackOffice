package com.sparta.backoffice.post.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.sparta.backoffice.post.repository.PostImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j(topic = "PostImage 스케쥴러")
@RequiredArgsConstructor
public class PostImageScheduleService {

    private final AmazonS3Client amazonS3Client;

    private final PostImageRepository postImageRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Scheduled(cron = "0 0 2 1 * *") // 매달 1일마다 2시에 데이터 무결성 체크
    public void checkDataIntegrity() {
        try {
            // S3에서 파일 목록 가져오기
            List<S3ObjectSummary> s3Objects = amazonS3Client.listObjects(
                    new ListObjectsRequest().withBucketName(bucket)).getObjectSummaries();

            // S3에만 존재하는 파일 찾고 삭제하기
            for (S3ObjectSummary s3Object : s3Objects) {
                if (postImageRepository.findPostImageByImageUrl(s3Object.getKey()).isEmpty()) {
                    amazonS3Client.deleteObject(bucket, s3Object.getKey());
                }
            }
        } catch (Exception e) {
            log.error("예외 발생: {}", e.getMessage());
        }
    }
}
