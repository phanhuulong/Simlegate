package com.smilegate.game.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MinioResponse {
    private String url;

    public static MinioResponse mapToResponse(String url) {
        return MinioResponse.builder()
                .url(url)
                .build();
    }
}
