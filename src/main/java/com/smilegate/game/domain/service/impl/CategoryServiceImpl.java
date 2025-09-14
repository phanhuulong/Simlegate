package com.smilegate.game.domain.service.impl;

import com.smilegate.game.data.entities.Category;
import com.smilegate.game.data.repository.CategoryRepository;
import com.smilegate.game.domain.model.BaseResponse;
import com.smilegate.game.domain.model.CategoryRequest;
import com.smilegate.game.domain.model.CategoryResponse;
import com.smilegate.game.domain.service.CategoryService;
import com.smilegate.game.utils.MessageService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final MessageService messageService;

    public BaseResponse<CategoryResponse> save(CategoryRequest category) {
        if(categoryRepository.existsByName(category.getName())) {
            return BaseResponse.error(400, messageService.getMessage("category.create.fail"));
        }
        Category categoryEntity = Category.builder()
                .name(category.getName())
                .code(category.getCode())
                .build();
        Category savedCategory = categoryRepository.save(categoryEntity);
        return BaseResponse.success(CategoryResponse.mapToResponse(savedCategory), messageService.getMessage("category.create.success"));
    }


}
