package com.smilegate.game.domain.controller;

import com.smilegate.game.domain.model.BaseResponse;
import com.smilegate.game.domain.model.CategoryRequest;
import com.smilegate.game.domain.model.CategoryResponse;
import com.smilegate.game.domain.service.CategoryService;
import com.smilegate.game.utils.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/category")
@RestController
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<BaseResponse<CategoryResponse>> save(@RequestBody CategoryRequest category) {
        return ResponseEntity.ok(categoryService.save(category));
    }


}
