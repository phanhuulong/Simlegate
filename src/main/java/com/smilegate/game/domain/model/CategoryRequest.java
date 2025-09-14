package com.smilegate.game.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class CategoryRequest {
    private String code;
    private String name;
}
