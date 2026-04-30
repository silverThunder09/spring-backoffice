package com.sparta.cch.backofficeproject.admin.dto;

import com.sparta.cch.backofficeproject.admin.entity.Admin;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AdminMyProfileResponse {

    private final String name;
    private final String email;
    private final String phone;

    @Builder
    public AdminMyProfileResponse(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }


    public static AdminMyProfileResponse of(Admin admin) {
        return AdminMyProfileResponse.builder()
                .name(admin.getName())
                .email(admin.getEmail())
                .phone(admin.getPhone())
                .build();
    }
}
