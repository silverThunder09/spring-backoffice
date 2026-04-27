package com.sparta.cch.backofficeproject.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주문 생성 요청 데이터를 전달하는 DTO 클래스
 */
@Getter
@NoArgsConstructor
public class OrderCreateRequestDto {

    /**
     * 주문할 고객 ID
     */
    @NotNull(message = "고객 ID는 필수입니다.")
    private Long customerId;

    /**
     * 주문할 상품 ID
     */
    @NotNull(message = "상품 ID는 필수입니다.")
    private Long productId;

    /**
     * 주문 수량
     */
    @NotNull(message = "주문 수량은 필수입니다.")
    @Min(value = 1, message = "주문 수량은 1 이상이어야 합니다.")
    private Integer quantity;
}
