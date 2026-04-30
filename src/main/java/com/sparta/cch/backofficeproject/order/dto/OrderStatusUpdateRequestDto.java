package com.sparta.cch.backofficeproject.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 주문 상태 변경 요청 데이터를 담는 DTO 클래스
 */
@Getter
@Setter
@NoArgsConstructor
public class OrderStatusUpdateRequestDto {

    /**
     * 변경할 주문 상태
     */
    @NotBlank(message = "변경할 주문 상태는 필수입니다.")
    private String status;
}
