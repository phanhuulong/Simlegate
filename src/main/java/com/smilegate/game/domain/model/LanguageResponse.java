package com.smilegate.game.domain.model;

import com.smilegate.game.data.entities.Language;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LanguageResponse {
    private String name;
    private String code;
    private boolean isDefault;

    public static LanguageResponse mapToResponse(Language language) {
        return LanguageResponse.builder()
                .name(language.getName())
                .code(language.getCode())
                .isDefault(language.isDefault())
                .build();
    }
}
