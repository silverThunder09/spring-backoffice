package com.sparta.cch.backofficeproject.customer.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CustomerDeleteResponse {

    private final Long customerId;

    @Builder
    public CustomerDeleteResponse(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * 삭제된 고객 ID를 고객 삭제 응답 DTO로 변환합니다.
     *
     * @param customerId 삭제된 고객 ID
     * @return 고객 삭제 응답 데이터
     */
    public static CustomerDeleteResponse of(Long customerId) {
        return CustomerDeleteResponse.builder()
                .customerId(customerId)
                .build();
    }
}
