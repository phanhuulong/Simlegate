package com.smilegate.game.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MinioResponse {
    private String fileName;

    public static MinioResponse mapToResponse(String fileName) {
        return MinioResponse.builder()
                .fileName(fileName)
                .build();
    }
}
