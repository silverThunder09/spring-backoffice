package com.sparta.cch.backofficeproject.customer.controller;

import com.sparta.cch.backofficeproject.admin.dto.AdminApiResponse;
import com.sparta.cch.backofficeproject.customer.dto.*;
import com.sparta.cch.backofficeproject.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 고객 ID로 특정 고객의 기본 정보를 수정합니다.
     *
     * @param customerId 수정할 고객 ID
     * @param request 고객 정보 수정 요청 데이터
     * @return 고객 정보 수정 결과 응답
     */
    @PatchMapping("/{customerId}")
    public ResponseEntity<AdminApiResponse<CustomerUpdateResponse>> updateCustomer(
            @PathVariable Long customerId,
            @Valid @RequestBody CustomerUpdateRequest request
    ) {
        AdminApiResponse<CustomerUpdateResponse> response = customerService.updateCustomer(customerId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 고객 ID로 특정 고객의 상태를 변경합니다.
     *
     * @param customerId 상태를 변경할 고객 ID
     * @param request 고객 상태 변경 요청 데이터
     * @return 고객 상태 변경 결과 응답
     */
    @PatchMapping("/{customerId}/status")
    public ResponseEntity<AdminApiResponse<CustomerStatusUpdateResponse>> updateCustomerStatus(
            @PathVariable Long customerId,
            @Valid @RequestBody CustomerStatusUpdateRequest request
    ) {
        AdminApiResponse<CustomerStatusUpdateResponse> response = customerService.updateCustomerStatus(customerId, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}

