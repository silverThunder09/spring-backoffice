package com.sparta.cch.backofficeproject.product.entity;

import com.sparta.cch.backofficeproject.admin.entity.Admin;
import com.sparta.cch.backofficeproject.common.entity.BaseEntity;
import com.sparta.cch.backofficeproject.product.enums.ProductCategory;
import com.sparta.cch.backofficeproject.product.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@SQLDelete(sql = "UPDATE products SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @Column(nullable = false, length = 255)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ProductCategory category;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer stock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ProductStatus status;

    @Column(length = 255)
    private String description;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    public Product(Admin admin, String name, ProductCategory category, Integer price, Integer stock, ProductStatus status, String description) {
        this.admin = admin;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.status = status;
        this.description = description;
    }

    // 상품 수정 메서드
    public void update(String name, ProductCategory category, Integer price, String description) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
    }

    /**
     * 상품 재고 변경 및 상태 자동 갱신 로직
     * @param newStock 변경할 재고 수량
     */
    public void updateStock(Integer newStock) {
        this.stock = newStock;

        // 상태 자동 갱신
        if (this.status != ProductStatus.DISCONTINUED) {
            if (this.stock == 0) {
                this.status = ProductStatus.SOLD_OUT; // 가용 재고 0이면 품절
            } else if (this.stock > 0) {
                this.status = ProductStatus.SALE; // 가용 재고 1 이상이면 판매중
            }
        }
    }

    /**
     * 상품 상태 수동 변경
     * @param status 변경할 상태 (SALE, SOLD_OUT, DISCONTINUED)
     */
    public void updateStatus(ProductStatus status) {
        this.status = status;
    }

    /**
     * 논리 삭제 헬퍼 메서드
     */
    public void deleted() {
        this.deletedAt = LocalDateTime.now();
    }
}
