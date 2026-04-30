package com.sparta.cch.backofficeproject.order.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 주문 목록 조회 시 검색/필터/정렬/페이징 조건을 담는 DTO 클래스
 */
@Getter
@Setter
@NoArgsConstructor
public class OrderListSearchRequestDto {

    /**
     * 주문번호 또는 고객명 검색어
     */
    private String keyword;

    /**
     * 주문 상태 필터
     */
    private String status;

    /**
     * 페이지 번호
     * 기본값: 1
     */
    @Min(value = 1, message = "페이지 번호는 1 이상이어야 합니다.")
    private Integer page = 1;

    /**
     * 페이지당 개수
     * 기본값: 10
     */
    @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
    private Integer size = 10;

    /**
     * 정렬 기준
     * 기본값: orderedAt
     */
    private String sortBy = "orderedAt";

    /**
     * 정렬 방향
     * 기본값: desc
     */
    private String direction = "desc";
}
