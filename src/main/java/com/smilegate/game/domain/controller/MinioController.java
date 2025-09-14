package com.smilegate.game.domain.controller;

import com.smilegate.game.domain.model.BaseResponse;
import com.smilegate.game.domain.model.MinioResponse;
import com.smilegate.game.domain.service.MinioService;
import com.smilegate.game.utils.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.RequestPath;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/minio")
@AllArgsConstructor
public class MinioController {
    private final MinioService minioService;
    private final MessageService messageService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse<MinioResponse>> uploadFile(@RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(minioService.uploadFile(file));
    }

    @GetMapping("/url")
    public ResponseEntity<BaseResponse<MinioResponse>> getFileUrl(@RequestParam("key") String key) {
        return ResponseEntity.ok(minioService.getPresignedUrl(key));
    }
}
