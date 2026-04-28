package com.sparta.cch.backofficeproject.product.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sparta.cch.backofficeproject.product.entity.Product;
import com.sparta.cch.backofficeproject.product.enums.ProductStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonPropertyOrder
public class ProductStatusUpdateResponse {

    private final Long id;
    private final ProductStatus status;
    private final LocalDateTime updatedAt;

    public static ProductStatusUpdateResponse of(Product product) {
        return ProductStatusUpdateResponse.builder()
                .id(product.getId())
                .status(product.getStatus())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
