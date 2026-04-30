package com.sparta.cch.backofficeproject.product.dto;

import com.sparta.cch.backofficeproject.product.entity.Product;
import com.sparta.cch.backofficeproject.product.enums.ProductCategory;
import com.sparta.cch.backofficeproject.product.enums.ProductStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 상품 정보 응답 DTO
 * 클라이언트에게 반환할 상품 데이터를 담습니다.
 */
@Getter
public class ProductCreateResponse {

    private final Long id;
    private final Long adminId;
    private final String name;
    private final ProductCategory category;
    private final Integer price;
    private final Integer stock;
    private final ProductStatus status;
    private final String description;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    @Builder
    public ProductCreateResponse(
            Long id,
            Long adminId,
            String name,
            ProductCategory category,
            Integer price,
            Integer stock,
            ProductStatus status,
            String description,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        this.id = id;
        this.adminId = adminId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.status = status;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 엔티티 객체를 응답 DTO로 변환합니다.
     * @param product 저장된 상품 Entity
     * @return 변환된 ProductResponse DTO
     */
    public static ProductCreateResponse of(Product product) {
        return ProductCreateResponse.builder()
                .id(product.getId())
                .adminId(product.getAdmin().getId())
                .name(product.getName())
                .category(product.getCategory())
                .price(product.getPrice())
                .stock(product.getStock())
                .status(product.getStatus())
                .description(product.getDescription())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
