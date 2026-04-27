package com.sparta.cch.backofficeproject.admin.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AdminApiResponse<T> {

    private final int status;
    private final String message;
    private final T data;

    @Builder
    public AdminApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> AdminApiResponse<T> success(int status, String message, T data) {
        return AdminApiResponse.<T>builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
    }
}
