package com.sparta.cch.backofficeproject.customer.dto;

import com.sparta.cch.backofficeproject.customer.entity.Customer;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CustomerUpdateResponse {

    private final Long customerId;
    private final String name;
    private final String email;
    private final String phone;
    private final String status;
    private final LocalDateTime updatedAt;

    @Builder
    public CustomerUpdateResponse(
            Long customerId,
            String name,
            String email,
            String phone,
            String status,
            LocalDateTime updatedAt
    ) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.updatedAt = updatedAt;
    }

    public static CustomerUpdateResponse of(Customer customer) {
        return CustomerUpdateResponse.builder()
                .customerId(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .status(customer.getStatus().name())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }
}
