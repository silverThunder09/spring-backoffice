package com.sparta.cch.backofficeproject.product.dto;

import com.sparta.cch.backofficeproject.product.entity.Product;
import com.sparta.cch.backofficeproject.product.enums.ProductCategory;
import com.sparta.cch.backofficeproject.product.enums.ProductStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ProductSimpleResponse {
    private final Long id;
    private final String name;
    private final ProductCategory category;
    private final Integer price;
    private final Integer stock;
    private final ProductStatus status;
    private final LocalDateTime createdAt;
    private final String adminName;

    public static ProductSimpleResponse of(Product product) {
        return ProductSimpleResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .price(product.getPrice())
                .stock(product.getStock())
                .status(product.getStatus())
                .createdAt(product.getCreatedAt())
                .adminName(product.getAdmin() != null ? product.getAdmin().getName() : "알 수 없음")
                .build();
    }
}