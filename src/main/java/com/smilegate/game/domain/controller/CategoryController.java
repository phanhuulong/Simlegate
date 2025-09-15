package com.smilegate.game.domain.controller;

import com.smilegate.game.domain.model.*;
import com.smilegate.game.domain.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RequestMapping("/category")
@RestController
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<CategoryResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @PostMapping
    public ResponseEntity<BaseResponse<CategoryResponse>> save(@Valid @RequestBody CategoryRequest category) {
        return ResponseEntity.ok(categoryService.save(category));
    }

    @GetMapping("/search")
    public ResponseEntity<BasePagingResponse<CategoryResponse>> searchCategory(@ParameterObject @ModelAttribute BasePagingRequest request) {
        return ResponseEntity.ok(categoryService.searchByNameAndCodeAndDeletedAtIsNull(request.getPageNumber(), request.getPageSize(), request.getKeyword()));
    }

    @DeleteMapping("/delete-many")
    public ResponseEntity<BaseResponse<String>> softDeleteByIds(@RequestBody List<UUID> ids) {
        return ResponseEntity.ok(categoryService.softDeleteByIds(ids));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<CategoryResponse>> update(@PathVariable UUID id, @Valid @RequestBody CategoryRequest category) {
        return ResponseEntity.ok(categoryService.update(id, category));
    }
}
