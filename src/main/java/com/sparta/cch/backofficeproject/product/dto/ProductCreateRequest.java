package com.sparta.cch.backofficeproject.product.dto;

import com.sparta.cch.backofficeproject.admin.entity.Admin;
import com.sparta.cch.backofficeproject.product.entity.Product;
import com.sparta.cch.backofficeproject.product.enums.ProductCategory;
import com.sparta.cch.backofficeproject.product.enums.ProductStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
//@Builder
public class ProductCreateRequest {

    @NotBlank(message = "상품명은 필수 입력값입니다.")
    @Size(min = 1, max = 255, message = "상품명은 1자 이상 255자 이하여야 합니다.")
    private String name;

    @NotNull(message = "유효하지 않은 카테고리입니다. (허용값: ELECTRONICS, FOOD, FASHION)")
    private ProductCategory category;

    @NotNull(message = "가격은 필수 입력값입니다.")
    @Min(value = 0, message = "가격은 0원 이어야 합니다.")
    private Integer price;

    @NotNull(message = "재고 수량은 필수 입력값입니다.")
    @Min(value = 0, message = "재고 수량은 0 이상이어야 합니다.")
    private Integer stock;

    @NotNull(message = "상품 상태는 필수 입력값입니다.")
    private ProductStatus status;

    @Size(max = 255, message = "상세 설명은 최대 255자까지 입력 가능합니다.")
    private String description;

    /**
     * DTO 데이터를 바탕으로 상품(Product) Entity 객체를 생성합니다.
     * @param admin 상품을 등록하는 관리자 객체
     * @return 조립된 Product 엔티티
     */
    public Product toEntity(Admin admin) {
        return Product.builder()
                .admin(admin)
                .name(this.name)
                .category(this.category)
                .price(this.price)
                .stock(this.stock)
                .status(this.status)
                .description(this.description)
                .build();
    }
}
