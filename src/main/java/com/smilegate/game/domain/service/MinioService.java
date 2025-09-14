package com.smilegate.game.domain.service;

import com.smilegate.game.domain.model.BaseResponse;
import com.smilegate.game.domain.model.MinioResponse;
import io.minio.GetObjectResponse;
import org.springframework.web.multipart.MultipartFile;

public interface MinioService {
    BaseResponse<MinioResponse> uploadFile(MultipartFile file);
    BaseResponse<MinioResponse> getPresignedUrl(String objectName);
}
