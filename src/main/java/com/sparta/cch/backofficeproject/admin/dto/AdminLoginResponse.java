package com.sparta.cch.backofficeproject.admin.dto;


import com.sparta.cch.backofficeproject.admin.entity.Admin;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AdminLoginResponse {

    private final Long adminId;
    private final String email;
    private final String role;
    private final String status;

    @Builder
    public AdminLoginResponse(Long adminId, String email, String role, String status) {
        this.adminId = adminId;
        this.email = email;
        this.role = role;
        this.status = status;
    }

    public static AdminLoginResponse create(Admin admin) {
        return AdminLoginResponse.builder()
                .adminId(admin.getId())
                .email(admin.getEmail())
                .role(admin.getRole().name())
                .status(admin.getStatus().name())
                .build();
    }
}

