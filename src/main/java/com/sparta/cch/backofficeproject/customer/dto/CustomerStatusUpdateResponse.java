package com.sparta.cch.backofficeproject.customer.dto;

import com.sparta.cch.backofficeproject.customer.entity.Customer;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CustomerStatusUpdateResponse {

    private final Long customerId;
    private final String status;
    private final LocalDateTime updatedAt;

    @Builder
    public CustomerStatusUpdateResponse(
            Long customerId,
            String status,
            LocalDateTime updatedAt
    ) {
        this.customerId = customerId;
        this.status = status;
        this.updatedAt = updatedAt;
    }

    public static CustomerStatusUpdateResponse of(Customer customer) {
        return CustomerStatusUpdateResponse.builder()
                .customerId(customer.getId())
                .status(customer.getStatus().name())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }
}

