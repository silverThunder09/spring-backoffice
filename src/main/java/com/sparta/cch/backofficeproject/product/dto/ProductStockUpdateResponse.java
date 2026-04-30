package com.sparta.cch.backofficeproject.product.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sparta.cch.backofficeproject.product.entity.Product;
import com.sparta.cch.backofficeproject.product.enums.ProductStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonPropertyOrder({"id", "stock", "status", "updatedAt"})
public class ProductStockUpdateResponse {

    private final Long id;
    private final Integer stock;
    private final ProductStatus status;
    private final LocalDateTime updatedAt;

    @Builder
    public ProductStockUpdateResponse(
            Long id,
            Integer stock,
            ProductStatus status,
            LocalDateTime updatedAt) {
        this.id = id;
        this.stock = stock;
        this.status = status;
        this.updatedAt = updatedAt;
    }

    public static ProductStockUpdateResponse of(Product product) {
        return ProductStockUpdateResponse.builder()
                .id(product.getId())
                .stock(product.getStock())
                .status(product.getStatus())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
