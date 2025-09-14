package com.smilegate.game.domain.service.impl;

import com.smilegate.game.domain.model.BaseResponse;
import com.smilegate.game.domain.model.CategoryResponse;
import com.smilegate.game.domain.model.MinioResponse;
import com.smilegate.game.domain.service.MinioService;
import com.smilegate.game.utils.MessageService;
import io.minio.*;
import io.minio.http.Method;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {
    private final MinioClient minioClient;
    private final MessageService messageService;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Override
    public BaseResponse<MinioResponse> uploadFile(MultipartFile file) {
        try{
            String fileName = file.getOriginalFilename();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return BaseResponse.success(MinioResponse.mapToResponse(fileName), messageService.getMessage("minio.upload.success"));
        } catch (Exception e) {
            return BaseResponse.error(400, messageService.getMessage("minio.upload.error"));
        }

    }

    @Override
    public BaseResponse<MinioResponse> getPresignedUrl(String objectName) {
        try {
            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(60 * 60)
                            .build()
            );
            return BaseResponse.success(MinioResponse.mapToResponse(url), messageService.getMessage("minio.getImage.success"));
        } catch (Exception e) {
            return BaseResponse.error(400, messageService.getMessage("minio.getImage.error"));
        }
    }
}
