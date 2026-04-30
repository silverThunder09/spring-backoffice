package com.sparta.cch.backofficeproject.admin.dto;

import com.sparta.cch.backofficeproject.admin.entity.Admin;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminMyProfileUpdateResponse {

    private final String name;
    private final String email;
    private final String phone;
    private final LocalDateTime updatedAt;

    @Builder
    public AdminMyProfileUpdateResponse(
            String name,
            String email,
            String phone,
            LocalDateTime updatedAt
    ) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.updatedAt = updatedAt;
    }

    public static AdminMyProfileUpdateResponse of(Admin admin) {
        return AdminMyProfileUpdateResponse.builder()
                .name(admin.getName())
                .email(admin.getEmail())
                .phone(admin.getPhone())
                .updatedAt(admin.getUpdatedAt())
                .build();
    }
}
