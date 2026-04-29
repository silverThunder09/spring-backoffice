package com.sparta.cch.backofficeproject.admin.dto;

import lombok.Getter;

@Getter
public class AdminDeleteResponse {

    private final Long adminId;

    public AdminDeleteResponse(Long adminId) {
        this.adminId = adminId;
    }

    public static AdminDeleteResponse of(Long adminId) {
        return new AdminDeleteResponse(adminId);
    }
}
