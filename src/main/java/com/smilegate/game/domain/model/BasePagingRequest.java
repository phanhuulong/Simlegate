package com.smilegate.game.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Setter;

@Setter
@Schema(description = "Paging request with keyword, page number, and page size")
public class BasePagingRequest {
    @Schema(description = "Search keyword (optional)")
    private String keyword;

    @Schema(defaultValue = "1")
    private int pageNumber = 1;

    @Schema(defaultValue = "10")
    private int pageSize = 10;

    public String getKeyword() {
        return keyword != null ? keyword.trim() : null;
    }

    public int getPageNumber() {
        return pageNumber < 1 ? 1 : pageNumber;
    }

    public int getPageSize() {
        return pageSize < 1 ? 10 : pageSize;
    }
}
