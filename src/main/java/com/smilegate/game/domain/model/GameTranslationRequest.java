package com.smilegate.game.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameTranslationRequest {
    @NotBlank
    @Size(min = 1, max = 255)
    private String name;
    @Size(max = 512)
    private String shortDescription;
    @Size(max = 5000)
    private String longDescription;
    @NotBlank
    @Size(min = 2, max = 10)
    private String languageCode;
}
