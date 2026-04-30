package com.sparta.cch.backofficeproject.order.dto;

import com.sparta.cch.backofficeproject.order.entity.Order;
import com.sparta.cch.backofficeproject.order.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 주문 목록 조회 시 주문 1건의 정보를 담는 DTO 클래스
 */
@Getter
@Builder
public class OrderListItemResponseDto {

    /**
     * 주문 고유 식별자
     */
    private final Long id;

    /**
     * 주문 번호
     */
    private final String orderNo;

    /**
     * 고객 이름
     */
    private final String customerName;

    /**
     * 상품 이름
     */
    private final String productName;

    /**
     * 주문 수량
     */
    private final Integer quantity;

    /**
     * 주문 총 금액
     */
    private final Integer totalPrice;

    /**
     * 주문 상태
     */
    private final OrderStatus status;

    /**
     * 주문 생성 일시
     */
    private final LocalDateTime orderedAt;

    /**
     * 등록 관리자 이름
     * CS 주문이 아니면 null 가능
     */
    private final String adminName;

    /**
     * Order 엔티티를 주문 목록의 주문 1건 응답 DTO로 변환합니다.
     *
     * @param order 조회한 주문 엔티티
     * @return 주문 목록 아이템 응답 DTO
     */
    public static OrderListItemResponseDto of(Order order) {

        return OrderListItemResponseDto.builder()
                .id(order.getId())
                .orderNo(order.getOrderNo())
                .customerName(order.getCustomer().getName())
                .productName(order.getProduct().getName())
                .quantity(order.getQuantity())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .orderedAt(order.getOrderedAt())
                .adminName(order.getAdmin() != null ? order.getAdmin().getName() : null)
                .build();
    }
}
