package com.sparta.cch.backofficeproject.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 주문 취소 요청 데이터를 담는 DTO 클래스
 */
@Getter
@Setter
@NoArgsConstructor
public class OrderCancelRequestDto {

    /**
     * 주문 취소 사유
     */
    @NotBlank(message = "주문 취소 사유는 필수 입력값입니다.")
    private String cancelReason;
}
