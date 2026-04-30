package com.sparta.cch.backofficeproject.order.dto;

import com.sparta.cch.backofficeproject.order.entity.Order;
import com.sparta.cch.backofficeproject.order.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 주문 상태 변경 응답 데이터를 담는 DTO 클래스
 */
@Getter
@Builder
public class OrderStatusUpdateResponseDto {

    /**
     * 주문 고유 식별자
     */
    private final Long id;

    /**
     * 주문 번호
     */
    private final String orderNo;

    /**
     * 변경 전 주문 상태
     */
    private final OrderStatus previousStatus;

    /**
     * 변경 후 주문 상태
     */
    private final OrderStatus currentStatus;

    /**
     * 상태 변경 시각
     */
    private final LocalDateTime updatedAt;

    /**
     * Order 엔티티와 변경 전 주문 상태를 주문 상태 변경 응답 DTO로 변환합니다.
     *
     * @param order          상태 변경이 완료된 주문 엔티티
     * @param previousStatus 변경 전 주문 상태
     * @return 주문 상태 변경 응답 DTO
     */
    public static OrderStatusUpdateResponseDto of(Order order, OrderStatus previousStatus) {

        return OrderStatusUpdateResponseDto.builder()
                .id(order.getId())
                .orderNo(order.getOrderNo())
                .previousStatus(previousStatus)
                .currentStatus(previousStatus)
                .updatedAt(LocalDateTime.now())
                .build();
    }
}