package com.sparta.cch.backofficeproject.product.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sparta.cch.backofficeproject.product.entity.Product;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import java.util.List;

/**
 * 상품 목록 페이징 응답 DTO
 * 클라이언트에게 페이징 처리된 상품 목록 데이터를 반환합니다.
 */
@Getter
@JsonPropertyOrder({"content", "pageInfo"})
public class ProductPageResponse {

    private final List<ProductSimpleResponse> content;
    private final PageInfo pageInfo;

    @Builder
    public ProductPageResponse(
            List<ProductSimpleResponse> content,
            PageInfo pageInfo) {
        this.content = content;
        this.pageInfo = pageInfo;
    }

    public static ProductPageResponse of(Page<Product> page) {
        PageInfo pageInfo = PageInfo.builder()
                .currentPage(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();

        return ProductPageResponse.builder()
                .content(page.getContent().stream().map(ProductSimpleResponse::of).toList())
                .pageInfo(pageInfo)
                .build();
    }

    /**
     * 페이징 정보 DTO
     */
    @Getter
    @JsonPropertyOrder({"currentPage", "pageSize", "totalElements", "totalPages"})
    public static class PageInfo {

        private final int currentPage;
        private final int pageSize;
        private final long totalElements;
        private final int totalPages;

        @Builder
        public PageInfo(int currentPage, int pageSize, long totalElements, int totalPages) {
            this.currentPage = currentPage;
            this.pageSize = pageSize;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
        }
    }
}