package com.sparta.cch.backofficeproject.order.dto;

import com.sparta.cch.backofficeproject.order.entity.Order;
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

    /**
     * Order 엔티티를 주문 생성 응답 DTO로 변환합니다.
     *
     * @param order 저장된 주문 엔티티
     * @return 주문 생성 응답 DTO
     */
    public static OrderCreateResponseDto of(Order order) {
        return OrderCreateResponseDto.builder()
                .orderId(order.getId())
                .orderNo(order.getOrderNo())
                .customerId(order.getCustomer().getId())
                .productId(order.getProduct().getId())
                .quantity(order.getQuantity())
                .orderPrice(order.getOrderPrice())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .orderedAt(order.getOrderedAt())
                .build();
    }
}
