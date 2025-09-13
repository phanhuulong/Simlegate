package com.smilegate.game.domain.controller;

import com.smilegate.game.domain.model.BaseResponse;
import com.smilegate.game.domain.service.MinioService;
import com.smilegate.game.utils.MessageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.RequestPath;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/minio")
public class MinioController {
    private MinioService minioService;
    private MessageService messageService;

    public MinioController(MinioService minioService, MessageService messageService) {
        this.minioService = minioService;
        this.messageService = messageService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse<String>> uploadFile(@RequestPart("file") MultipartFile file) {
        try {
            String objectKey = minioService.uploadFile(file);
            String message = messageService.getMessage("minio.upload.success");
            return ResponseEntity.ok(BaseResponse.success(objectKey, message));
        } catch (Exception e) {
            String message = messageService.getMessage("minio.upload.error", e.getMessage());
            return ResponseEntity.internalServerError().body(BaseResponse.error(500, message));
        }
    }

    @GetMapping("/url")
    public ResponseEntity<BaseResponse<String>> getFileUrl(@RequestParam("key") String key) {
        try {
            String url = minioService.getPresignedUrl(key);
            String message = messageService.getMessage("minio.getImage.success");
            return ResponseEntity.ok(BaseResponse.success(url, message));
        } catch (Exception e) {
            String message = messageService.getMessage("minio.getImage.error", e.getMessage());
            return ResponseEntity.internalServerError().body(BaseResponse.error(500, message));
        }
    }
}
