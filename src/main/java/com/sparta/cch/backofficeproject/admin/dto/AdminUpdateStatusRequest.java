package com.sparta.cch.backofficeproject.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
    public class AdminUpdateStatusRequest {

    @NotBlank(message = "상태 값은 필수입니다.")
    private String status;
}
