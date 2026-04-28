package com.sparta.cch.backofficeproject.order.dto;

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
}