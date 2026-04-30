package com.sparta.cch.backofficeproject.product.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
@JsonPropertyOrder({"id", "name", "category", "price", "stock", "status", "description", "createdAt", "adminName", "adminEmail"})
public class ProductGetDetailResponse {

    private final Long id;
    private final String name;
    private final ProductCategory category;
    private final Integer price;
    private final Integer stock;
    private final ProductStatus status;
    private final String description;
    private final LocalDateTime createdAt;
    private final String adminName;
    private final String adminEmail;

    @Builder
    public ProductGetDetailResponse(
            Long id,
            String name,
            ProductCategory category,
            Integer price,
            Integer stock,
            ProductStatus status,
            String description,
            LocalDateTime createdAt,
            String adminName,
            String adminEmail) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.status = status;
        this.description = description;
        this.createdAt = createdAt;
        this.adminName = adminName;
        this.adminEmail = adminEmail;
    }

    public static ProductGetDetailResponse of(Product product) {
        // Admin이 null일 경우 NullPointerException을 방지하기 위해, 검증합니다.
        String safeAdminName = (product.getAdmin() != null) ? product.getAdmin().getName() : "알 수 없음";
        String safeAdminEmail = (product.getAdmin() != null) ? product.getAdmin().getEmail() : "알 수 없음";

        return ProductGetDetailResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .price(product.getPrice())
                .stock(product.getStock())
                .status(product.getStatus())
                .description(product.getDescription())
                .createdAt(product.getCreatedAt())
                .adminName(safeAdminName)
                .adminEmail(safeAdminEmail)
                .build();
    }
}
