package com.sparta.cch.backofficeproject.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class AdminChangePasswordRequest {

    @NotBlank(message = "현재 비밀번호는 필수 입력값입니다.")
    private String currentPassword;

    @NotBlank(message = "새 비밀번호는 필수 입력값입니다.")
    @Size(min = 8, message = "비밀번호는 8자 이상 입력 해주세요")
    private String newPassword;

    @NotBlank(message = "새 비밀번호 확인은 필수 입력값입니다.")
    private String newPasswordConfirm;
}
