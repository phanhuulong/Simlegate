package com.smilegate.game.domain.service;

import io.minio.GetObjectResponse;
import org.springframework.web.multipart.MultipartFile;

public interface MinioService {
    String uploadFile(MultipartFile file) throws Exception;
    String getPresignedUrl(String objectName) throws Exception;
}
