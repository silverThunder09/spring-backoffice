package com.sparta.cch.backofficeproject.admin.dto;

import com.sparta.cch.backofficeproject.admin.entity.Admin;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminUpdateRoleResponse {
    private final Long adminId;
    private final String role;
    private final LocalDateTime updatedAt;

    @Builder
    public AdminUpdateRoleResponse(Long adminId, String role, LocalDateTime updatedAt) {
        this.adminId = adminId;
        this.role = role;
        this.updatedAt = updatedAt;
    }

    public static AdminUpdateRoleResponse of(Admin admin) {
        return AdminUpdateRoleResponse.builder()
                .adminId(admin.getId())
                .role(admin.getRole().name())
                .updatedAt(admin.getUpdatedAt())
                .build();
    }

}
