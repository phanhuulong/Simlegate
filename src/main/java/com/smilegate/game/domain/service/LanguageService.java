package com.smilegate.game.domain.service;

import com.smilegate.game.domain.model.BaseResponse;
import com.smilegate.game.domain.model.LanguageRequest;
import com.smilegate.game.domain.model.LanguageResponse;

public interface LanguageService {
    BaseResponse<LanguageResponse> save(LanguageRequest lang);
}
