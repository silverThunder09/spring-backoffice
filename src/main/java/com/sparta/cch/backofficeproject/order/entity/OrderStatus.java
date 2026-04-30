package com.sparta.cch.backofficeproject.order.entity;

/**
 * 주문 상태를 관리하는 enum 클래스
 *
 * PENDING : 준비중
 * SHIPPING : 배송중
 * COMPLETED : 배송완료
 * CANCELED : 취소됨
 */
public enum OrderStatus {
    PENDING,
    SHIPPING,
    COMPLETED,
    CANCELED
}
