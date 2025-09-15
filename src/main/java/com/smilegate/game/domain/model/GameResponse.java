package com.smilegate.game.domain.model;

import com.smilegate.game.data.entities.Category;
import com.smilegate.game.data.entities.Game;
import com.smilegate.game.data.entities.GameTranslation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;




@Data
@AllArgsConstructor
@Builder
public class GameResponse {
    private String sku;
    private String name;
    private String shortDescription;
    private String longDescription;
    private String image;
    private String languageName;
    private String categoryName;
    private String createdBy;

    public static GameResponse mapToResponse(Game game, GameTranslation translation) {
        Category category = game.getCategories().stream().findFirst().orElse(null);

        return GameResponse.builder()
                .sku(game.getSku())
                .name(translation != null ? translation.getName() : null)
                .createdBy(game.getCreateBy() != null ? game.getCreateBy() : null)
                .shortDescription(translation != null ? translation.getShortDescription() : null)
                .longDescription(translation != null ? translation.getLongDescription() : null)
                .image(game.getImageUrl())
                .languageName(translation != null && translation.getLanguage() != null ? translation.getLanguage().getName() : null)
                .categoryName(category != null ? category.getName() : null)
                .build();
    }
}
