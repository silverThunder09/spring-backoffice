package com.sparta.cch.backofficeproject.order.dto;

import com.sparta.cch.backofficeproject.order.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

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
}
