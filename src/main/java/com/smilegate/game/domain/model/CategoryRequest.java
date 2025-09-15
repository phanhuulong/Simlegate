package com.smilegate.game.domain.model;

import com.smilegate.game.data.entities.Game;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryRequest {
    @NotBlank
    @Size(min = 1, max = 255)
    private String code;
    @NotBlank
    @Size(min = 1, max = 255)
    private String name;
}
