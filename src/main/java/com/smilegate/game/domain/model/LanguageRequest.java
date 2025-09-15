package com.smilegate.game.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LanguageRequest {
    @NotBlank
    @Size(min = 1, max = 10)
    private String code;
    @NotBlank
    @Size(min = 1, max = 255)
    private String name;
}
