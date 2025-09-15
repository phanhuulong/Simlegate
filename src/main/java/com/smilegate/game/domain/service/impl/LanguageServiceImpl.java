package com.smilegate.game.domain.service.impl;

import com.smilegate.game.data.entities.Language;
import com.smilegate.game.data.repository.LanguageRepository;
import com.smilegate.game.domain.model.BasePagingResponse;
import com.smilegate.game.domain.model.BaseResponse;
import com.smilegate.game.domain.model.LanguageRequest;
import com.smilegate.game.domain.model.LanguageResponse;
import com.smilegate.game.domain.service.LanguageService;
import com.smilegate.game.utils.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class LanguageServiceImpl implements LanguageService {
    private final LanguageRepository languageRepository;
    private final MessageService messageService;

    @Override
    public BaseResponse<LanguageResponse> save(LanguageRequest lang) {
        if(languageRepository.existsByCode(lang.getCode())) {
            return BaseResponse.error(400, messageService.getMessage("language.create.code.fail", lang.getCode()));
        }
        if(languageRepository.existsByName(lang.getName())) {
            return BaseResponse.error(400, messageService.getMessage("language.create.name.fail", lang.getName()));
        }

        Language languageEntity = Language.builder()
                .code(lang.getCode())
                .name(lang.getName())
                .isDefault(false)
                .build();
        Language savedLang = languageRepository.save(languageEntity);
        return BaseResponse.success(LanguageResponse.mapToResponse(savedLang), messageService.getMessage("language.create.success"));
    }

    @Override
    public BaseResponse<LanguageResponse> update(String code, LanguageRequest lang) {
        Optional<Language> existingLangOpt = languageRepository.findByCodeAndDeletedAtIsNull(code);
        if(existingLangOpt.isEmpty()) {
            return BaseResponse.error(400, messageService.getMessage("language.notfound", code));
        }
        if (languageRepository.existsByCode(lang.getCode()) && !lang.getCode().equals(code)) {
            return BaseResponse.error(400, messageService.getMessage("language.update.code.fail", lang.getCode()));
        }
        if (languageRepository.existsByName(lang.getName()) && !lang.getName().equals(existingLangOpt.get().getName())) {
            return BaseResponse.error(400, messageService.getMessage("language.update.name.fail", lang.getName()));
        }

        Language existingLang = existingLangOpt.get();
        existingLang.setName(lang.getName());
        existingLang.setCode(lang.getCode());
        Language updatedLang = languageRepository.save(existingLang);
        return BaseResponse.success(LanguageResponse.mapToResponse(updatedLang), messageService.getMessage("language.update.success"));
    }

    @Override
    public BasePagingResponse<LanguageResponse> searchByNameAndCodeAndDeletedAtIsNull(int pageNumber, int pageSize, String keyword) {
        try {
            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

            Page<Language> languagesPage = languageRepository.searchByNameAndCodeAndDeletedAtIsNull(keyword, pageable);
            List<LanguageResponse> languageResponses = languagesPage.getContent().stream()
                    .map(LanguageResponse::mapToResponse)
                    .toList();
            return BasePagingResponse.success(languageResponses, (int) languagesPage.getTotalElements(), pageSize, pageNumber, List.of(messageService.getMessage("language.getall.success")), null);
        } catch (Exception e) {
            return BasePagingResponse.error(400, e.getMessage());
        }
    }

    @Override
    public BaseResponse<String> softDeleteByCodes(List<String> codes) {
        List<Language> existing = languageRepository.findAllByCodeInAndDeletedAtIsNull(codes);
        Set<String> existingCodes = existing.stream().map(Language::getCode).collect(Collectors.toSet());

        List<String> nonExistentCodes = codes.stream()
                .filter(code -> !existingCodes.contains(code))
                .toList();

        if (!nonExistentCodes.isEmpty()) {
            return BaseResponse.error(400, messageService.getMessage("language.delete.nonexistent.fail", String.join(", ", nonExistentCodes)));
        }

        for(Language lang : existing) {
            if(lang.isDefault()) {
                return BaseResponse.error(400, messageService.getMessage("language.delete.default.fail", lang.getCode()));
            }
        }
        int deletedCount = languageRepository.softDeleteByCodes(codes);

        if (deletedCount == codes.size()) {
            return BaseResponse.success(null, messageService.getMessage("language.delete.success", deletedCount));
        } else {
            List<String> failedToDeleteCodes = codes.stream()
                    .filter(code -> !languageRepository.findByCodeAndDeletedAtIsNull(code).isEmpty())
                    .toList();
            String message = messageService.getMessage("language.delete.partial.success", deletedCount, failedToDeleteCodes.size(), String.join(", ", failedToDeleteCodes));
            return BaseResponse.success(null, message);
        }
    }

    @Override
    public BaseResponse<LanguageResponse> setDefaultLanguage(String code) {
        Optional<Language> langOpt = languageRepository.findByCodeAndDeletedAtIsNull(code);
        if(langOpt.isEmpty()) {
            return BaseResponse.error(400, messageService.getMessage("language.notfound", code));
        }
        Language lang = langOpt.get();
        if(lang.isDefault()) {
            return BaseResponse.success(LanguageResponse.mapToResponse(lang), messageService.getMessage("language.setdefault.success"));
        }
        // unset default for all languages
        List<Language> allLangs = languageRepository.findAllByDeletedAtIsNull();
        for(Language l : allLangs) {
            l.setDefault(false);
        }
        languageRepository.saveAll(allLangs);
        // set default for the selected language
        lang.setDefault(true);
        Language updatedLang = languageRepository.save(lang);
        return BaseResponse.success(LanguageResponse.mapToResponse(updatedLang), messageService.getMessage("language.setdefault.success"));
    }
}

