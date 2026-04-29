package com.sparta.cch.backofficeproject.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AdminUpdateRoleRequest {

    @NotBlank(message = "역할 값은 필수 입니다.")
    private String role;
}
