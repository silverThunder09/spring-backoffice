package com.sparta.cch.backofficeproject.customer.controller;

import com.sparta.cch.backofficeproject.admin.dto.AdminApiResponse;
import com.sparta.cch.backofficeproject.customer.dto.CustomerDetailResponse;
import com.sparta.cch.backofficeproject.customer.dto.CustomerListRequest;
import com.sparta.cch.backofficeproject.customer.dto.CustomerListResponse;
import com.sparta.cch.backofficeproject.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admins/customers")
public class CustomerController {

    private final CustomerService customerService;

    /**
     * 고객 목록을 조회합니다.
     * 검색, 페이징, 정렬, 상태 필터를 지원합니다.
     *
     * @param request 고객 목록 조회 요청 정보
     * @return 고객 목록 조회 결과 응답
     */
    @GetMapping
    public ResponseEntity<AdminApiResponse<CustomerListResponse>> getAll(
            @Valid CustomerListRequest request
    ) {
        AdminApiResponse<CustomerListResponse> response = customerService.getAll(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 고객 ID로 특정 고객의 상세 정보를 조회합니다.
     *
     * @param customerId 조회할 고객 ID
     * @return 고객 상세 조회 결과 응답
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<AdminApiResponse<CustomerDetailResponse>> getCustomer(
            @PathVariable Long customerId
    ) {
        AdminApiResponse<CustomerDetailResponse> response = customerService.getCustomer(customerId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}

