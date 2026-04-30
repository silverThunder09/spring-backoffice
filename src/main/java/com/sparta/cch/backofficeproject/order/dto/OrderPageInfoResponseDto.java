package com.sparta.cch.backofficeproject.order.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

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

    /**
     * Page 객체와 현재 요청 페이지 번호를 페이징 응답 DTO로 변환합니다.
     *
     * @param page 조회된 page 객체
     * @param currentPage 클라이언트가 요청한 현재 페이지 번호
     * @return 페이징 정보 응답 DTO
     */
    public static OrderPageInfoResponseDto of(Page<?> page, Integer currentPage) {
        return OrderPageInfoResponseDto.builder()
                .currentPage(currentPage)
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
