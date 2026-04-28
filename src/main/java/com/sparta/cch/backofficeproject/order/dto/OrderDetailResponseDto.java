package com.sparta.cch.backofficeproject.order.dto;

import com.sparta.cch.backofficeproject.order.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 주문 상세 조회 응답 데이터를 담는 DTO 클래스
 */
@Getter
@Builder
public class OrderDetailResponseDto {

    /**
     * 주문 고유 식별자
     */
    private final Long id;

    /**
     * 주문번호
     */
    private final String orderNo;

    /**
     * 고객 고유 식별자
     */
    private final Long customerId;

    /**
     * 고객 이름
     */
    private final String customerName;

    /**
     * 고객 이메일
     */
    private final String customerEmail;

    /**
     * 상품 고유 식별자
     */
    private final Long productId;

    /**
     * 상품명
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
     * 등록 관리자 ID
     * 고객 직접 주문이면 null 가능
     */
    private final Long adminId;

    /**
     * 등록 관리자 이름
     * 고객 직접 주문이면 null 가능
     */
    private final String adminName;

    /**
     * 주문 취소 사유
     * 취소 주문만 값이 존재할 수 있음
     */
    private final String cancelReason;
}
