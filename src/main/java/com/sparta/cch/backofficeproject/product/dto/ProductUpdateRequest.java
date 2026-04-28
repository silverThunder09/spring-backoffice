package com.sparta.cch.backofficeproject.product.dto;

import com.sparta.cch.backofficeproject.product.entity.Product;
import com.sparta.cch.backofficeproject.product.enums.ProductCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateRequest {
    @NotBlank(message = "상품명은 필수 입력값입니다.")
    @Size(min = 1, max = 255, message = "상품명은 1자 이상 255자 이하여야 합니다.")
    private String name;

    @NotNull(message = "유효하지 않은 카테고리입니다.")
    private ProductCategory category;

    @NotNull(message = "가격은 필수 입력값입니다.")
    @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
    private Integer price;

    @Size(max = 255, message = "상세 설명은 최대 255자까지 입력 가능합니다.")
    private String description;
}
