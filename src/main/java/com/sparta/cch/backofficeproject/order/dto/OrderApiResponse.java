package com.sparta.cch.backofficeproject.order.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * 주문 API 공통 응답을 감싸는 DTO 클래스
 *
 * @param <T> 실제 응답 데이터 타입
 */
@Getter
public class OrderApiResponse<T> {

    /**
     * HTTP 상태 코드
     */
    private final int status;

    /**
     * 응답 메시지
     */
    private final String message;

    /**
     * 실제 응답 데이터
     */
    private final T data;

    /**
     * 주문 API 공통 응답 객체를 생성하는 생성자
     */
    @Builder
    public OrderApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    /**
     * 성공 응답 객체를 생성하는 정적 메서드
     */
    public static <T> OrderApiResponse<T> success(int status, String message, T data) {
        return OrderApiResponse.<T>builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
    }
}
