package com.smilegate.game.domain.service;

import com.smilegate.game.domain.model.*;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    BaseResponse<CategoryResponse> save(CategoryRequest category);
    BaseResponse<CategoryResponse> update(UUID id,CategoryRequest category);
    BaseResponse<CategoryResponse> getById(UUID id);
    BasePagingResponse<CategoryResponse> searchByNameAndCodeAndDeletedAtIsNull(int pageNumber, int pageSize, String keyword);
    BaseResponse<String> softDeleteByIds(List<UUID> ids);
}
