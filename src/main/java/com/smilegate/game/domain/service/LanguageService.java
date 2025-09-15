package com.smilegate.game.domain.service;

import com.smilegate.game.domain.model.BasePagingResponse;
import com.smilegate.game.domain.model.BaseResponse;
import com.smilegate.game.domain.model.LanguageRequest;
import com.smilegate.game.domain.model.LanguageResponse;

import java.util.List;

public interface LanguageService {
    BaseResponse<LanguageResponse> save(LanguageRequest lang);
    BasePagingResponse<LanguageResponse> searchByNameAndCodeAndDeletedAtIsNull(int pageNumber, int pageSize, String keyword);
    BaseResponse<LanguageResponse> update(String code, LanguageRequest lang);
    BaseResponse<String> softDeleteByCodes(List<String> codes);
    BaseResponse<LanguageResponse> setDefaultLanguage(String code);
}
