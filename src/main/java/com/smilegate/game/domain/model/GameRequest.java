package com.smilegate.game.domain.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;
import java.util.UUID;
import com.smilegate.game.Enums.StatusGame;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameRequest {
    @NotBlank
    @Size(min = 1, max = 255)
    private String sku;
    @NotBlank
    @Size(min = 1, max = 255)
    private String createdBy;
    @NotEmpty
    @Valid
    private List<GameTranslationRequest> translations;
    @NotEmpty
    private List<UUID> categoryIds;
    @NotNull
    private StatusGame status;
}
