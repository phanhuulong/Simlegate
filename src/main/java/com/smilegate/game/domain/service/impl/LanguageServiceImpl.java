package com.smilegate.game.domain.service.impl;

import com.smilegate.game.data.repository.LanguageRepository;
import com.smilegate.game.domain.model.BaseResponse;
import com.smilegate.game.domain.model.LanguageRequest;
import com.smilegate.game.domain.model.LanguageResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LanguageServiceImpl {
    private final LanguageRepository languageRepository;

//    public BaseResponse<LanguageRequest> save(LanguageResponse lang) {
//        if (lang.isDefault()) {
//            Optional<LanguageResponse> existingDefault = languageRepository.findByIsDefaultTrue();
//            if (existingDefault.isPresent() && !existingDefault.get().getCode().equals(lang.getCode())) {
//                throw new IllegalStateException("There can only be one default language.");
//            }
//        }
//        return languageRepository.save(lang);
//    }

}
