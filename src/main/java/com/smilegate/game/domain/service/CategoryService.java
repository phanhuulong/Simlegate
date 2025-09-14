package com.smilegate.game.domain.service;

import com.smilegate.game.domain.model.BaseResponse;
import com.smilegate.game.domain.model.CategoryRequest;
import com.smilegate.game.domain.model.CategoryResponse;

public interface CategoryService {
    BaseResponse<CategoryResponse> save(CategoryRequest category);
//    BaseResponse<CategoryResponse> update(CategoryRequest category);
//    BaseResponse<CategoryResponse> delete(Long id);
//    BaseResponse<CategoryResponse> getById(Long id);
//    BaseResponse<CategoryResponse> getAll();
}
