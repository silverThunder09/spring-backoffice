package com.sparta.cch.backofficeproject.admin.dto;

import com.sparta.cch.backofficeproject.admin.entity.Admin;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Getter
public class AdminListItemResponse {
    private Long adminId;
    private String name;
    private String email;
    private String phone;
    private String role;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;

    @Builder
    public AdminListItemResponse(
            Long adminId,
            String name,
            String email,
            String phone,
            String role,
            String status,
            LocalDateTime createdAt,
            LocalDateTime approvedAt)
    {
        this.adminId = adminId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.approvedAt = approvedAt;
    }

    public static AdminListItemResponse create(Admin admin) {
        return AdminListItemResponse.builder()
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
