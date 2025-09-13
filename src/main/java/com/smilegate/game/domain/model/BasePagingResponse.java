package com.smilegate.game.domain.model;

import lombok.Getter;

import java.util.List;

@Getter
public class BasePagingResponse<T> {
    private int status;
    private int pageNumber;
    private int pageSize;
    private int countItems;
    private int countPages;
    private List<T> data;
    private List<String> message;
    private Object error;

    public static <T> BasePagingResponse<T> success(List<T> data, int totalItems, int pageSize, int pageNumber, List<String> message, String error) {
        int countPages = (int) Math.ceil((double) totalItems / pageSize);
        BasePagingResponse<T> response = new BasePagingResponse<>();
        response.status = 200;
        response.data = data;
        response.pageNumber = pageNumber;
        response.pageSize = pageSize;
        response.countItems = totalItems;
        response.countPages = countPages;
        response.message = message;
        response.error = error;
        return response;
    }

    public static <T> BasePagingResponse<T> error(int status, String errorMessage) {
        BasePagingResponse<T> response = new BasePagingResponse<>();
        response.status = status;
        response.data = null;
        response.pageNumber = 0;
        response.pageSize = 0;
        response.countItems = 0;
        response.countPages = 0;
        response.message = List.of();
        response.error = errorMessage;
        return response;
    }
}
