package com.sparta.cch.backofficeproject.customer.dto;

import com.sparta.cch.backofficeproject.customer.entity.Customer;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CustomerListItemResponse {

    private Long customerId;
    private String name;
    private String email;
    private String phone;
    private String status;
    private LocalDateTime createdAt;

    @Builder
    public CustomerListItemResponse(
            Long customerId,
            String name,
            String email,
            String phone,
            String status,
            LocalDateTime createdAt
    ) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static CustomerListItemResponse of(Customer customer) {
        return CustomerListItemResponse.builder()
                .customerId(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .status(customer.getStatus().name())
                .createdAt(customer.getCreatedAt())
                .build();
    }
}

