package com.sparta.cch.backofficeproject.order.dto;

import com.sparta.cch.backofficeproject.order.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 주문 생성 응답 데이터를 전달하는 DTO 클래스
 */
@Getter
@Builder
public class OrderCreateResponseDto {

    /**
     * 주문 ID
     */
    private final Long orderId;

    /**
     * 주문 번호
     */
    private final String orderNo;

    /**
     * 고객 ID
     */
    private final Long customerId;

    /**
     * 상품 ID
     */
    private final Long productId;

    /**
     * 주문 수량
     */
    private final Integer quantity;

    /**
     * 주문 당시 상품 가격
     */
    private final Integer orderPrice;

    /**
     * 총 주문 금액
     */
    private final Integer totalPrice;

    /**
     * 주문 상태
     */
    private final OrderStatus status;

    /**
     * 주문 일시
     */
    private final LocalDateTime orderedAt;
}
