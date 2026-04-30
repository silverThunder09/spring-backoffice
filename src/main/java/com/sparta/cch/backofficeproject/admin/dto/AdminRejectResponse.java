package com.sparta.cch.backofficeproject.admin.dto;


import com.sparta.cch.backofficeproject.admin.entity.Admin;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminRejectResponse {

    private final Long adminId;
    private final String name;
    private final String email;
    private final String phone;
    private final String role;
    private final String status;
    private final LocalDateTime createdAt;
    private final LocalDateTime approvedAt;
    private final LocalDateTime updatedAt;

    @Builder
    public AdminRejectResponse(
            Long adminId,
            String name,
            String email,
            String phone,
            String role,
            String status,
            LocalDateTime createdAt,
            LocalDateTime approvedAt,
            LocalDateTime updatedAt) {

        this.adminId = adminId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.approvedAt = approvedAt;
        this.updatedAt = updatedAt;
    }

    public static AdminRejectResponse of(Admin admin) {
        return AdminRejectResponse.builder()
                .adminId(admin.getId())
                .name(admin.getName())
                .email(admin.getEmail())
                .phone(admin.getPhone())
                .role(admin.getRole().name())
                .status(admin.getStatus().name())
                .createdAt(admin.getCreatedAt())
                .approvedAt(admin.getApprovedAt())
                .updatedAt(admin.getUpdatedAt())
                .build();
    }
}
