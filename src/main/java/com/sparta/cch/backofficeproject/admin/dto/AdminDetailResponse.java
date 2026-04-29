package com.sparta.cch.backofficeproject.admin.dto;


import com.sparta.cch.backofficeproject.admin.entity.Admin;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminDetailResponse {

    private final Long adminId;
    private final String name;
    private final String email;
    private final String phone;
    private final String role;
    private final String status;
    private final LocalDateTime createdAt;
    private final LocalDateTime approvedAt;

    @Builder
    public AdminDetailResponse(
            Long adminId,
            String name,
            String email,
            String phone,
            String role,
            String status,
            LocalDateTime createdAt,
            LocalDateTime approvedAt
    ) {
        this.adminId = adminId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.approvedAt = approvedAt;
    }

    public static AdminDetailResponse of(Admin admin) {
        return AdminDetailResponse.builder()
                .adminId(admin.getId())
                .name(admin.getName())
                .email(admin.getEmail())
                .phone(admin.getPhone())
                .role(admin.getRole().name())
                .status(admin.getStatus().name())
                .createdAt(admin.getCreatedAt())
                .approvedAt(admin.getApprovedAt())
                .build();
    }
}

