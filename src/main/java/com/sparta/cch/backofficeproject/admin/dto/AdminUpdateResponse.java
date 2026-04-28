package com.sparta.cch.backofficeproject.admin.dto;

import com.sparta.cch.backofficeproject.admin.entity.Admin;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminUpdateResponse {

    private final Long adminId;
    private final String name;
    private final String email;
    private final String phone;
    private final String role;
    private final String status;
    private final LocalDateTime updatedAt;

    @Builder
    public AdminUpdateResponse(
            Long adminId,
            String name,
            String email,
            String phone,
            String role,
            String status,
            LocalDateTime updatedAt)
    {

        this.adminId = adminId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.status = status;
        this.updatedAt = updatedAt;
    }

    public static AdminUpdateResponse of(Admin admin) {
        return AdminUpdateResponse.builder()
                .adminId(admin.getId())
                .name(admin.getName())
                .email(admin.getEmail())
                .phone(admin.getPhone())
                .role(admin.getRole().name())
                .status(admin.getStatus().name())
                .updatedAt(admin.getUpdatedAt())
                .build();
    }
}
