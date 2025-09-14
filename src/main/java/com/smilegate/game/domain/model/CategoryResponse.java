package com.smilegate.game.domain.model;

import com.smilegate.game.data.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
@Builder
public class CategoryResponse {
    private String code;
    private String name;
    private Instant createdAt;
    private Instant deletedAt;

    public static CategoryResponse mapToResponse(Category category) {
        return CategoryResponse.builder()
                .code(category.getCode())
                .name(category.getName())
                .createdAt(category.getCreatedAt())
                .deletedAt(category.getDeletedAt())
                .build();
    }
}
