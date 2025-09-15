package com.smilegate.game.domain.service;

import com.smilegate.game.domain.model.BasePagingResponse;
import com.smilegate.game.domain.model.BaseResponse;
import com.smilegate.game.domain.model.GameRequest;
import com.smilegate.game.domain.model.GameResponse;

import java.util.List;
import java.util.UUID;

public interface GameService {
    BaseResponse<GameResponse> createGame(GameRequest request);
    BaseResponse<GameResponse> updateGame(UUID id, GameRequest request);
    BasePagingResponse<GameResponse> searchGame(int pageNumber, int pageSize, String keyword, String acceptLanguage);
    BaseResponse<String> softDeleteByIds(List<UUID> ids);
    BaseResponse<GameResponse> getGameById(UUID id, String acceptLanguage);
}
