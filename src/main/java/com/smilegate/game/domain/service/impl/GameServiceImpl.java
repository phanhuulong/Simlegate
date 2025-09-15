package com.smilegate.game.domain.service.impl;

import com.smilegate.game.Enums.StatusGame;
import com.smilegate.game.data.entities.Category;
import com.smilegate.game.data.entities.GameTranslation;
import com.smilegate.game.data.entities.Game;
import com.smilegate.game.data.entities.Language;
import com.smilegate.game.data.repository.CategoryRepository;
import com.smilegate.game.data.repository.GameRepository;
import com.smilegate.game.data.repository.LanguageRepository;
import com.smilegate.game.domain.model.*;
import com.smilegate.game.domain.service.GameService;
import com.smilegate.game.utils.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final MessageService messageService;
    private final LanguageRepository languageRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public BaseResponse<GameResponse> getGameById(UUID id, String acceptLanguage) {
        try {
            Game game = gameRepository.findByIdAndStatusEquals(id, StatusGame.ACTIVE)
                    .orElseThrow(() -> new RuntimeException(messageService.getMessage("game.notfound")));

            Language preferredLanguage = languageRepository.findByCodeAndDeletedAtIsNull(acceptLanguage)
                    .orElseGet(() -> languageRepository.findByIsDefaultTrueAndDeletedAtIsNull()
                            .orElseThrow(() -> new RuntimeException(messageService.getMessage("language.default.notfound"))));

            GameTranslation selectedTranslation = game.getTranslations().stream()
                    .filter(gt -> gt.getLanguage().getCode().equals(preferredLanguage.getCode()))
                    .findFirst()
                    .orElse(game.getTranslations().stream()
                            .filter(gt -> gt.getLanguage().isDefault())
                            .findFirst()
                            .orElse(game.getTranslations().isEmpty() ? null : game.getTranslations().iterator().next()));

            if (selectedTranslation == null) {
                throw new RuntimeException(messageService.getMessage("game.translation.notfound"));
            }

            return BaseResponse.success(GameResponse.mapToResponse(game, selectedTranslation), messageService.getMessage("game.get.success"));
        } catch (Exception e) {
            return BaseResponse.error(400, messageService.getMessage("game.get.fail", e.getMessage()));
        }
    }

    @Override
    public BaseResponse<GameResponse> createGame(GameRequest request) {
        try {
            if (gameRepository.existsBySku(request.getSku())) {
                return BaseResponse.error(400, messageService.getMessage("game.create.fail", "SKU " + request.getSku() + " already exists"));
            }
            // Upload image to MinIO, Don't effect on swagger
//            BaseResponse<MinioResponse> uploadResponse = minioService.uploadFile(request.getImage());
//            if (uploadResponse.getStatus() != 200) {
//                return BaseResponse.error(400, messageService.getMessage("game.create.fail"));
//            }

            Set<Category> categories = new HashSet<>(categoryRepository.findAllByIdInAndDeletedAtIsNull(request.getCategoryIds()));
            if (categories.size() != request.getCategoryIds().size()) {
                return BaseResponse.error(400, messageService.getMessage("category.notfound"));
            }

            Game game = Game.builder()
                    .sku(request.getSku())
                    .createBy(request.getCreatedBy())
                    .status(StatusGame.ACTIVE)
//                    .imageUrl(uploadResponse.getData().getFileName())
                    .imageUrl(null)
                    .build();

            for (GameTranslationRequest translationRequest : request.getTranslations()) {
                Language language = languageRepository.findByCodeAndDeletedAtIsNull(translationRequest.getLanguageCode())
                        .orElseThrow(() -> new RuntimeException(messageService.getMessage("language.notfound", translationRequest.getLanguageCode())));

                GameTranslation gameTranslation = GameTranslation.builder()
                        .game(game)
                        .language(language)
                        .name(translationRequest.getName())
                        .shortDescription(translationRequest.getShortDescription())
                        .longDescription(translationRequest.getLongDescription())
                        .build();
                game.getTranslations().add(gameTranslation);
            }

            game.setCategories(categories);

            Game savedGame = gameRepository.save(game);
            return BaseResponse.success(GameResponse.mapToResponse(savedGame, null), messageService.getMessage("game.create.success"));
        } catch (Exception e) {
            return BaseResponse.error(400, messageService.getMessage("game.create.fail", e.getMessage()));
        }
    }

    @Override
    public BaseResponse<GameResponse> updateGame(UUID id, GameRequest request) {
        try {
            Game game = gameRepository.findByIdAndStatusEquals(id, StatusGame.ACTIVE)
                    .orElseThrow(() -> new RuntimeException(messageService.getMessage("game.notfound")));

            if (request.getSku() != null && !request.getSku().equals(game.getSku())) {
                if (gameRepository.existsBySku(request.getSku())) {
                    return BaseResponse.error(400, messageService.getMessage("game.update.sku.fail", request.getSku()));
                }
                game.setSku(request.getSku());
            }

            if (request.getCreatedBy() != null) {
                game.setCreateBy(request.getCreatedBy());
            }

            if (request.getStatus() != null) {
                game.setStatus(request.getStatus());
            }

            if (request.getTranslations() != null && !request.getTranslations().isEmpty()) {
                for (GameTranslationRequest translationRequest : request.getTranslations()) {
                    Language language = languageRepository.findByCodeAndDeletedAtIsNull(translationRequest.getLanguageCode())
                            .orElseThrow(() -> new RuntimeException(messageService.getMessage("language.notfound", translationRequest.getLanguageCode())));

                    game.getTranslations().stream()
                            .filter(gt -> gt.getLanguage().getCode().equals(translationRequest.getLanguageCode()))
                            .findFirst()
                            .ifPresentOrElse(gameTranslation -> {
                                gameTranslation.setName(translationRequest.getName() != null ? translationRequest.getName() : gameTranslation.getName());
                                gameTranslation.setShortDescription(translationRequest.getShortDescription() != null ? translationRequest.getShortDescription() : gameTranslation.getShortDescription());
                                gameTranslation.setLongDescription(translationRequest.getLongDescription() != null ? translationRequest.getLongDescription() : gameTranslation.getLongDescription());
                            }, () -> {
                                GameTranslation newGameTranslation = GameTranslation.builder()
                                        .game(game)
                                        .language(language)
                                        .name(translationRequest.getName())
                                        .shortDescription(translationRequest.getShortDescription())
                                        .longDescription(translationRequest.getLongDescription())
                                        .build();
                                game.getTranslations().add(newGameTranslation);
                            });
                }
            }

            if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
                Set<Category> categories = new HashSet<>(categoryRepository.findAllByIdInAndDeletedAtIsNull(request.getCategoryIds()));
                if (categories.size() != request.getCategoryIds().size()) {
                    return BaseResponse.error(400, messageService.getMessage("category.notfound"));
                }
                game.setCategories(categories);
            }

            Game updatedGame = gameRepository.save(game);

            return BaseResponse.success(GameResponse.mapToResponse(updatedGame, null), messageService.getMessage("game.update.success"));
        } catch (Exception e) {
            return BaseResponse.error(400, messageService.getMessage("game.update.fail", e.getMessage()));
        }
    }

    @Override
    public BasePagingResponse<GameResponse> searchGame(int pageNumber, int pageSize, String keyword, String acceptLanguage) {
        try {
            Language preferredLanguage = languageRepository.findByCodeAndDeletedAtIsNull(acceptLanguage)
                    .orElseGet(() -> languageRepository.findByIsDefaultTrueAndDeletedAtIsNull()
                            .orElseThrow(() -> new RuntimeException(messageService.getMessage("language.default.notfound"))));

            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
            Page<Game> gamesPage = gameRepository.searchBySkuAndCategoryNameAndNameAndDeletedAtIsNull(keyword, pageable, preferredLanguage.getCode());

            List<GameResponse> gameResponses = gamesPage.getContent().stream()
                    .map(game -> {
                        GameTranslation selectedTranslation = game.getTranslations().stream()
                                .filter(gt -> gt.getLanguage().getCode().equals(preferredLanguage.getCode()))
                                .findFirst()
                                .orElse(game.getTranslations().stream()
                                        .filter(gt -> gt.getLanguage().isDefault())
                                        .findFirst()
                                        .orElse(game.getTranslations().isEmpty() ? null : game.getTranslations().iterator().next()));

                        if (selectedTranslation == null) {
                            throw new RuntimeException(messageService.getMessage("game.translation.notfound"));
                        }

                        return GameResponse.mapToResponse(game, selectedTranslation);
                    })
                    .toList();

            return BasePagingResponse.success(gameResponses, (int) gamesPage.getTotalElements(), pageSize, pageNumber, List.of(messageService.getMessage("game.getall.success")), null);
        } catch (Exception e) {
            return BasePagingResponse.error(400, e.getMessage());
        }
    }

    @Override
    public BaseResponse<String> softDeleteByIds(List<UUID> ids) {
        try {
            // Get game active in ids
            List<Game> activeGames = gameRepository.findAllByIdInAndStatusEquals(ids, StatusGame.ACTIVE);
            Set<UUID> activeGameIds = activeGames.stream().map(Game::getId).collect(Collectors.toSet());

            // IDs game will be soft deleted
            List<UUID> idsToSoftDelete = ids.stream()
                    .filter(activeGameIds::contains)
                    .toList();

            List<UUID> failedIds = ids.stream()
                    .filter(id -> !activeGameIds.contains(id))
                    .toList();

            int deletedCount = 0;
            if (!idsToSoftDelete.isEmpty()) {
                deletedCount = gameRepository.softDeleteByIds(idsToSoftDelete);
            }

            if (failedIds.isEmpty()) {
                return BaseResponse.success(String.format(messageService.getMessage("game.delete.success"), deletedCount), messageService.getMessage("game.delete.success"));
            } else {
                // Get name of game translations that can't be deleted
                List<String> failedGameNames = failedIds.stream()
                        .map(id -> gameRepository.findByIdAndStatusEquals(id, StatusGame.DELETED))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .map(game -> game.getTranslations().stream()
                                .filter(gt -> gt.getLanguage().isDefault())
                                .findFirst()
                                .orElse(game.getTranslations().isEmpty() ? null : game.getTranslations().iterator().next()))
                        .filter(gt -> gt != null && gt.getName() != null)
                        .map(GameTranslation::getName)
                        .toList();

                String listIdFail = String.join(", ", failedGameNames);

                return BaseResponse.success(String.format(messageService.getMessage("game.delete.partial.success"), deletedCount, ids.size() - deletedCount) + ", " +
                        messageService.getMessage("game.delete.fail", listIdFail), null);
            }
        } catch (Exception e) {
            return BaseResponse.error(400, messageService.getMessage("game.delete.fail", e.getMessage()));
        }
    }
}
