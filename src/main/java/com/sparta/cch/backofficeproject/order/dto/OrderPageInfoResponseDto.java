package com.sparta.cch.backofficeproject.order.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * 주문 목록 조회 시 페이징 정보를 담는 DTO 클래스
 */
@Getter
@Builder
public class OrderPageInfoResponseDto {

    /**
     * 현재 페이지 번호
     */
    private final Integer currentPage;

    /**
     * 페이지당 개수
     */
    private final Integer pageSize;

    /**
     * 전체 데이터 개수
     */
    private final Long totalElements;

    /**
     * 전체 페이지 수
     */
    private final Integer totalPages;
}
