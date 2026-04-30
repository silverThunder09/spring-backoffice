package com.sparta.cch.backofficeproject.common.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommonResponse<T> {

    private final int status;
    private final String message;
    private final T data;

    @Builder
    public CommonResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> CommonResponse<T> success(int status, String message, T data) {
        return CommonResponse.<T>builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
    }
}
