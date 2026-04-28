package com.sparta.cch.backofficeproject.order.dto;

import com.sparta.cch.backofficeproject.order.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 주문 취소 응답 데이터를 담는 DTO 클래스
 */
@Getter
@Builder
public class OrderCancelResponseDto {

    /**
     * 주문 고유 식별자
     */
    private final Long id;

    /**
     * 주문 번호
     */
    private final String orderNo;

    /**
     * 취소 전 주문 상태
     */
    private final OrderStatus previousStatus;

    /**
     * 취소 후 주문 상태
     */
    private final OrderStatus currentStatus;

    /**
     * 주문 취소 사유
     */
    private final String cancelReason;

    /**
     * 주문 취소 시각
     */
    private final LocalDateTime updatedAt;
}