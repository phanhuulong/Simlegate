package com.smilegate.game.domain.service.impl;

import com.smilegate.game.data.entities.Category;
import com.smilegate.game.data.repository.CategoryRepository;
import com.smilegate.game.domain.model.*;
import com.smilegate.game.domain.service.CategoryService;
import com.smilegate.game.utils.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final MessageService messageService;

    @Override
    public BaseResponse<CategoryResponse> getById(UUID id) {
        try {
            Category category = categoryRepository.getByIdAndDeletedAtIsNull(id);
            if (category == null) {
                return BaseResponse.error(404, messageService.getMessage("category.notfound"));
            }
            return BaseResponse.success(CategoryResponse.mapToResponse(category), messageService.getMessage("category.getbyid.success"));
        }catch (Exception e) {
            return BaseResponse.error(404, messageService.getMessage("category.getbyid.fail", e.getMessage()));
        }
    }

    @Override
    public BaseResponse<CategoryResponse> save(CategoryRequest category) {
        if(categoryRepository.existsByName(category.getName())) {
            return BaseResponse.error(400, messageService.getMessage("category.create.fail", "Name " + category.getName() + " already exists"));
        }
        if(categoryRepository.existsByCode(category.getCode())) {
            return BaseResponse.error(400, messageService.getMessage("category.create.fail", "Code " + category.getCode() + " already exists"));
        }

        Category categoryEntity = Category.builder()
                .name(category.getName())
                .code(category.getCode())
                .build();
        Category savedCategory = categoryRepository.save(categoryEntity);
        return BaseResponse.success(CategoryResponse.mapToResponse(savedCategory), messageService.getMessage("category.create.success"));
    }

    @Override
    public BaseResponse<CategoryResponse> update(UUID id, CategoryRequest category) {
        if (!categoryRepository.existsByIdAndDeletedAtIsNull(id)) {
            return BaseResponse.error(400, messageService.getMessage("category.notfound"));
        }
        if (categoryRepository.existsByCodeAndIdNot(category.getCode(), id)) {
            return BaseResponse.error(400, messageService.getMessage("category.update.fail", "Code " + category.getCode() + " already exists"));
        }

        return categoryRepository.findById(id)
                .map(existingCategory -> {
                    existingCategory.setName(category.getName());
                    existingCategory.setCode(category.getCode());
                    Category updatedCategory = categoryRepository.save(existingCategory);
                    return BaseResponse.success(CategoryResponse.mapToResponse(updatedCategory), messageService.getMessage("category.update.success"));
                })
                .orElseGet(() -> BaseResponse.error(404, messageService.getMessage("category.update.fail", id.toString())));
    }

    @Override
    public BasePagingResponse<CategoryResponse> searchByNameAndCodeAndDeletedAtIsNull(int pageNumber, int pageSize, String keyword) {
        try {
            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

            Page<Category> categoryPage = categoryRepository.searchByNameAndCodeAndDeletedAtIsNull(keyword, pageable);
            List<CategoryResponse> categoryResponses = categoryPage.getContent().stream()
                    .map(CategoryResponse::mapToResponse)
                    .toList();
            return BasePagingResponse.success(categoryResponses, (int) categoryPage.getTotalElements(), pageSize, pageNumber, List.of(messageService.getMessage("category.getall.success")), null);
        }catch (Exception e) {
            return BasePagingResponse.error(400, e.getMessage());
        }
    }

    @Override
    public BaseResponse<String> softDeleteByIds(List<UUID> ids) {
        try {
            List<Category> existingActiveCategories = categoryRepository.findAllByIdInAndDeletedAtIsNull(ids);
            Set<UUID> activeCategoryIds = existingActiveCategories.stream().map(Category::getId).collect(Collectors.toSet());

            List<UUID> idsToSoftDelete = ids.stream()
                    .filter(activeCategoryIds::contains)
                    .toList();

            List<UUID> failedIds = ids.stream()
                    .filter(id -> !activeCategoryIds.contains(id))
                    .toList();

            int deletedCount = 0;
            if (!idsToSoftDelete.isEmpty()) {
                deletedCount = categoryRepository.softDeleteByIds(idsToSoftDelete);
            }

            if (failedIds.isEmpty()) {
                return BaseResponse.success(String.format(messageService.getMessage("category.delete.success"), deletedCount), messageService.getMessage("category.delete.success"));
            } else {
                List<String> failedCategoryNames = failedIds.stream()
                        .map(id -> categoryRepository.findById(id).orElse(null))
                        .filter(category -> category != null && category.getName() != null)
                        .map(Category::getName)
                        .toList();

                String listNameFail = String.join(", ", failedCategoryNames);

                return BaseResponse.success(String.format(messageService.getMessage("category.delete.partial.success"), deletedCount, ids.size() - deletedCount) + ", " +
                        messageService.getMessage("category.delete.fail", listNameFail), null);
            }
        } catch (Exception e) {
            return BaseResponse.error(500, messageService.getMessage("category.delete.fail", e.getMessage()));
        }
    }


}
