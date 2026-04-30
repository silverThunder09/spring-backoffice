package com.sparta.cch.backofficeproject.product.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sparta.cch.backofficeproject.product.entity.Product;
import com.sparta.cch.backofficeproject.product.enums.ProductStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonPropertyOrder
public class ProductStatusUpdateResponse {

    private final Long id;
    private final ProductStatus status;
    private final LocalDateTime updatedAt;

    @Builder
    public ProductStatusUpdateResponse(
            Long id,
            ProductStatus status,
            LocalDateTime updatedAt) {
        this.id = id;
        this.status = status;
        this.updatedAt = updatedAt;
    }

    public static ProductStatusUpdateResponse of(Product product) {
        return ProductStatusUpdateResponse.builder()
                .id(product.getId())
                .status(product.getStatus())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
