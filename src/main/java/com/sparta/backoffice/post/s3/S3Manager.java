package com.sparta.backoffice.post.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.sparta.backoffice.global.constant.ErrorCode;
import com.sparta.backoffice.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sparta.backoffice.post.service.PostImageService.POST_PATH_PREFIX;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Manager {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public String uploadMultipartFileWithPublicRead(String prefix, MultipartFile multipartFile) throws Exception {
        String fileName = multipartFile.getOriginalFilename();
        String fileNameExtension = StringUtils.getFilenameExtension(fileName);
        fileName = prefix + UUID.randomUUID() + "." + fileNameExtension;

        String fileUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + fileName;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(multipartFile.getContentType());
        metadata.setContentLength(multipartFile.getSize());

        PutObjectRequest putObjectRequest =
                new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead);
        amazonS3Client.putObject(putObjectRequest);
        return fileUrl;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void deletePostFile(String fileUrl) {
        amazonS3Client.deleteObject(bucket, extractKey(fileUrl));
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void deleteAllPostFiles(String postId) {
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName(bucket)
                .withPrefix(POST_PATH_PREFIX + postId + "/");

        List<S3ObjectSummary> objectSummaries = amazonS3Client.listObjects(listObjectsRequest).getObjectSummaries();

        for (S3ObjectSummary objSummary : objectSummaries) {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, objSummary.getKey()));
        }
    }

    private String extractKey(String fileUrl) {
        // "posts/"를 포함하여 그 이후의 모든 문자열을 추출하는 정규 표현식
        String regex = "(posts/.*)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fileUrl);

        // 정규 표현식에 매칭되는 부분 찾기
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new ApiException(ErrorCode.INVALID_POST_FILE_URL);
    }
}
