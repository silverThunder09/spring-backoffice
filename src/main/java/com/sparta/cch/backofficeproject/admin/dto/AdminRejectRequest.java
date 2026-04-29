package com.sparta.cch.backofficeproject.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class AdminRejectRequest {

    @NotBlank(message = "거부 사유는 필수 입력값입니다.")
    @Size(max = 255, message = "거부 사유는 200자 이하여야 합니다.")
    private String rejectReason;
}
