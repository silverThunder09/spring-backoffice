package com.sparta.cch.backofficeproject.product.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductDeleteResponse {

    private final String message;
    private final Long deleteId;

    public static ProductDeleteResponse of(Long id) {
        return ProductDeleteResponse.builder()
                .message("상품이 성공적으로 삭제되었습니다.")
                .deleteId(id)
                .build();
    }
}
