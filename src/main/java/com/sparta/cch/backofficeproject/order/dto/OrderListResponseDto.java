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
}
