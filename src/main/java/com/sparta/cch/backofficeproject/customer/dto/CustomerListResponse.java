package com.sparta.cch.backofficeproject.customer.dto;

import com.sparta.cch.backofficeproject.customer.entity.Customer;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class CustomerListResponse {

    private final List<CustomerListItemResponse> customers;
    private final int page;
    private final int size;
    private final long totalCount;
    private final int totalPages;

    @Builder
    public CustomerListResponse(
            List<CustomerListItemResponse> customers,
            int page,
            int size,
            long totalCount,
            int totalPages
    ) {
        this.customers = customers;
        this.page = page;
        this.size = size;
        this.totalCount = totalCount;
        this.totalPages = totalPages;
    }

    public static CustomerListResponse of(Page<Customer> customerPage) {
        List<CustomerListItemResponse> customers = customerPage.getContent().stream()
                .map(CustomerListItemResponse::of)
                .toList();

        return CustomerListResponse.builder()
                .customers(customers)
                .page(customerPage.getNumber() + 1)
                .size(customerPage.getSize())
                .totalCount(customerPage.getTotalElements())
                .totalPages(customerPage.getTotalPages())
                .build();
    }
}
