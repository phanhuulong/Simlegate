package com.smilegate.game.domain.model;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseResponse<T> {
    private int status;
    private T data;
    private String message;

    public static <T> BaseResponse<T> success(T data) {
        return BaseResponse.<T>builder()
                .message("Ok")
                .status(200)
                .data(data)
                .build();
    }

    public static <T> BaseResponse<T> success(T data, String message){
        return BaseResponse.<T>builder()
                .message(message)
                .status(200)
                .data(data)
                .build();
    }

    public static <T> BaseResponse<T> error(int status, String message) {
        return BaseResponse.<T>builder()
                .message(message)
                .status(status)
                .data(null)
                .build();
    }
    public static <T> BaseResponse<T> error(T data,int status, String message) {
        return BaseResponse.<T>builder()
                .message(message)
                .status(status)
                .data(data)
                .build();
    }
}
