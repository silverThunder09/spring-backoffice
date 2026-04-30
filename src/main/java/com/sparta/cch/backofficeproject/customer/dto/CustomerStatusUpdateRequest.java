package com.sparta.cch.backofficeproject.customer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CustomerStatusUpdateRequest {

    @NotBlank(message = "상태 값은 필수 입력값입니다.")
    private String status;
}
