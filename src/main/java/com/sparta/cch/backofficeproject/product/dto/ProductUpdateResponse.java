package com.sparta.cch.backofficeproject.product.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sparta.cch.backofficeproject.product.entity.Product;
import com.sparta.cch.backofficeproject.product.enums.ProductCategory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonPropertyOrder({"id", "name", "category", "price", "updatedAt"})
public class ProductUpdateResponse {

    private final Long id;
    private final String name;
    private final ProductCategory category;
    private final Integer price;
    private final LocalDateTime updatedAt;

    @Builder
    public ProductUpdateResponse(
            Long id,
            String name,
            ProductCategory category,
            Integer price,
            LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.updatedAt = updatedAt;
    }

    public static ProductUpdateResponse of(Product product) {
        return ProductUpdateResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .price(product.getPrice())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
