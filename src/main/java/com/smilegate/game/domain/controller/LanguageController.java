package com.smilegate.game.domain.controller;

import com.smilegate.game.domain.model.*;
import com.smilegate.game.domain.service.LanguageService;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/languages")
@AllArgsConstructor
public class LanguageController {
    private final LanguageService languageService;

    @PostMapping
    public BaseResponse<LanguageResponse> save(@Valid @RequestBody LanguageRequest lang) {
        return languageService.save(lang);
    }

    @PutMapping("/{code}")
    public BaseResponse<LanguageResponse> update(@PathVariable String code, @Valid @RequestBody LanguageRequest lang) {
        return languageService.update(code, lang);
    }

    @GetMapping("/search")
    public ResponseEntity<BasePagingResponse<LanguageResponse>> searchByNameAndCodeAndDeletedAtIsNull(@ParameterObject @ModelAttribute BasePagingRequest request) {
        return ResponseEntity.ok(languageService.searchByNameAndCodeAndDeletedAtIsNull(request.getPageNumber(), request.getPageSize(), request.getKeyword()));
    }

    @DeleteMapping("/delete-many")
    public BaseResponse<String> softDeleteByCodes(@RequestBody List<String> codes) {
        return languageService.softDeleteByCodes(codes);
    }

    @PutMapping("/set-default/{code}")
    public BaseResponse<LanguageResponse> setDefaultLanguage(@PathVariable String code) {
        return languageService.setDefaultLanguage(code);
    }
}
