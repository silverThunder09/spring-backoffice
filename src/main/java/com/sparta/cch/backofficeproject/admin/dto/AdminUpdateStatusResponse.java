package com.sparta.cch.backofficeproject.admin.dto;

import com.sparta.cch.backofficeproject.admin.entity.Admin;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminUpdateStatusResponse {
    private final Long adminId;
    private final String status;
    private final LocalDateTime updatedAt;

    @Builder
    public AdminUpdateStatusResponse(Long adminId, String status, LocalDateTime updatedAt) {
        this.adminId = adminId;
        this.status = status;
        this.updatedAt = updatedAt;
    }

    public static AdminUpdateStatusResponse of(Admin admin) {
        return AdminUpdateStatusResponse.builder()
                .adminId(admin.getId())
                .status(admin.getStatus().name())
                .updatedAt(admin.getUpdatedAt())
                .build();
    }
}
