package com.smilegate.game.domain.controller;

import com.smilegate.game.domain.model.*;
import com.smilegate.game.domain.service.GameService;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/games")
@AllArgsConstructor
public class GameController {
    private final GameService gameService;

    @PostMapping
    public ResponseEntity<BaseResponse<GameResponse>> createGame(@Valid @RequestBody GameRequest gameRequest) {
        return ResponseEntity.ok(gameService.createGame(gameRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<GameResponse>> updateGame(@PathVariable UUID id, @Valid @RequestBody GameRequest gameRequest) {
        return ResponseEntity.ok(gameService.updateGame(id, gameRequest));
    }

    @GetMapping("/search")
    public ResponseEntity<BasePagingResponse<GameResponse>> searchGame(
            @RequestHeader(value = "Accept-Language", required = false, defaultValue = "en") String acceptLanguage,
            @ParameterObject @ModelAttribute BasePagingRequest request) {
        return ResponseEntity.ok(gameService.searchGame(request.getPageNumber(), request.getPageSize(), request.getKeyword(), acceptLanguage));
    }

    @DeleteMapping("/delete-many")
    public ResponseEntity<BaseResponse<String>> softDeleteByIds(@RequestBody List<UUID> ids) {
        return ResponseEntity.ok(gameService.softDeleteByIds(ids));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<GameResponse>> getGameById(@PathVariable UUID id,
                                                                  @RequestHeader(value = "Accept-Language", required = false, defaultValue = "en") String acceptLanguage) {
        return ResponseEntity.ok(gameService.getGameById(id, acceptLanguage));
    }
}
