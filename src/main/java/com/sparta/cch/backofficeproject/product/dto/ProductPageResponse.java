package com.sparta.cch.backofficeproject.product.dto;

import com.sparta.cch.backofficeproject.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import java.util.List;

@Getter
@Builder
public class ProductPageResponse {
    private final List<ProductSimpleResponse> content;
    private final PageInfo pageInfo;

    public static ProductPageResponse of(Page<Product> page) {
        return ProductPageResponse.builder()
                .content(page.getContent().stream().map(ProductSimpleResponse::of).toList())
                .pageInfo(new PageInfo(page.getNumber() + 1, page.getSize(), page.getTotalElements(), page.getTotalPages()))
                .build();
    }

    @Getter
    @AllArgsConstructor
    public static class PageInfo {
        private int currentPage;
        private int pageSize;
        private long totalElements;
        private int totalPages;
    }
}