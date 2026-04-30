package com.sparta.cch.backofficeproject.order.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 주문 목록 조회 응답 전체를 담는 DTO 클래스
 */
@Getter
@Builder
public class OrderListResponseDto {

    /**
     * 주문 목록
     */
    private final List<OrderListItemResponseDto> orders;

    /**
     * 페이징 정보
     */
    private final OrderPageInfoResponseDto pageInfo;

    /**
     * 주문 목록과 페이징 정보를 주문 목록 조회 응답 DTO로 변환합니다.
     *
     * @param orders 주문 목록 응답 데이터
     * @param pageInfo 페이징 정보 응답 데이터
     * @return 주문 목록 조회 응답 DTO
     */
    public static OrderListResponseDto of(
            List<OrderListItemResponseDto> orders,
            OrderPageInfoResponseDto pageInfo
    ) {

        return OrderListResponseDto.builder()
                .orders(orders)
                .pageInfo(pageInfo)
                .build();
    }
}
