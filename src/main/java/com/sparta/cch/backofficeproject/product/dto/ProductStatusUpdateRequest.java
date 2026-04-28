package com.sparta.cch.backofficeproject.product.dto;

import com.sparta.cch.backofficeproject.product.enums.ProductStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductStatusUpdateRequest {

    @NotNull(message = "변경할 상태값은 필수입니다.")
    private ProductStatus status;
}
