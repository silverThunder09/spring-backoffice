package com.sparta.cch.backofficeproject.customer.service;

import com.sparta.cch.backofficeproject.common.exception.ApiException;
import com.sparta.cch.backofficeproject.common.exception.ErrorCode;
import com.sparta.cch.backofficeproject.customer.dto.*;
import com.sparta.cch.backofficeproject.customer.entity.Customer;
import com.sparta.cch.backofficeproject.customer.entity.CustomerStatus;
import com.sparta.cch.backofficeproject.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    /**
     * 고객 목록을 조회합니다.
     *
     * <p>keyword가 존재하면 이름/이메일 부분 일치 검색을 수행하고,
     * status가 존재하면 해당 조건으로 필터링합니다.
     * 모든 조건이 없으면 전체 목록을 반환합니다.</p>
     *
     * @param request 검색 키워드, 페이징, 정렬, 상태 필터 조건
     * @return 고객 목록 및 페이징 정보
     */
    @Transactional(readOnly = true)
    public CustomerListResponse getAll(CustomerListRequest request) {

        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getSize(),
                Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy())
        );

        String keyword = StringUtils.hasText(request.getKeyword())
                ? request.getKeyword().trim()
                : null;

        Page<Customer> customerPage = customerRepository.searchCustomers(
                keyword,
                request.getStatus(),
                pageable
        );

        return CustomerListResponse.of(customerPage);
    }

    /**
     * 고객 ID로 특정 고객의 상세 정보를 조회합니다.
     *
     * <p>고객 ID 유효성을 검증한 뒤 고객을 조회하고,
     * 조회된 엔티티를 상세 응답 DTO로 변환하여 반환합니다.</p>
     *
     * @param customerId 조회할 고객 ID
     * @return 고객 상세 조회 결과 응답
     */
    @Transactional(readOnly = true)
    public CustomerDetailResponse getCustomer(Long customerId) {

        Customer customer = findCustomerById(customerId);

        return CustomerDetailResponse.of(customer);
    }

    /**
     * 고객 ID로 고객을 조회합니다.
     *
     * @param customerId 조회할 고객 ID
     * @return 조회된 고객 엔티티
     * @throws ApiException 고객 ID가 1 미만이거나 null인 경우 (INVALID_CUSTOMER_ID)
     * @throws ApiException 고객이 존재하지 않는 경우 (CUSTOMER_NOT_FOUND)
     */
    private Customer findCustomerById(Long customerId) {
        if (customerId == null || customerId < 1) {
            throw new ApiException(ErrorCode.INVALID_CUSTOMER_ID);
        }

        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ApiException(ErrorCode.CUSTOMER_NOT_FOUND));
    }

    /**
     * 고객 ID로 특정 고객의 기본 정보를 수정합니다.
     *
     * <p>고객 ID 유효성을 검증하고, 이메일 중복 여부를 확인한 뒤
     * 이름, 이메일, 전화번호를 수정합니다.</p>
     *
     * @param customerId 수정할 고객 ID
     * @param request 고객 정보 수정 요청 데이터
     * @return 고객 정보 수정 결과 응답
     */
    @Transactional
    public CustomerUpdateResponse updateCustomer(Long customerId, CustomerUpdateRequest request) {

        Customer customer = findCustomerById(customerId);

        // 본인 이메일 제외하고 중복 체크
        if (!customer.getEmail().equals(request.getEmail())
                && customerRepository.existsByEmail(request.getEmail())) {
            throw new ApiException(ErrorCode.DUPLICATED_EMAIL);
        }

        customer.update(request.getName(), request.getEmail(), request.getPhone());

        return CustomerUpdateResponse.of(customer);
    }

    /**
     * 고객 ID로 특정 고객의 상태를 변경합니다.
     *
     * <p>고객 ID 유효성을 검증하고 고객을 조회 후 상태를 변경합니다.
     *
     * @param customerId 상태를 변경할 고객 ID
     * @param request 고객 상태 변경 요청 데이터
     * @return 고객 상태 변경 결과 응답
     */
    @Transactional
    public CustomerStatusUpdateResponse updateCustomerStatus(
            Long customerId,
            CustomerStatusUpdateRequest request
    ) {
        Customer customer = findCustomerById(customerId);

        CustomerStatus status;

        try {
            status = CustomerStatus.valueOf(request.getStatus().trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApiException(ErrorCode.INVALID_CUSTOMER_STATUS);
        }

        customer.updateStatus(status);

        return CustomerStatusUpdateResponse.of(customer);
    }

    /**
     * 고객 ID로 특정 고객을 삭제합니다.
     *
     * <p>고객 ID 유효성을 검증한 뒤 고객을 조회하고,
     * 이미 탈퇴 처리된 고객인지 확인한 후 soft delete를 수행합니다.</p>
     *
     * @param customerId 삭제할 고객 ID
     * @return 고객 삭제 결과 응답
     * @throws ApiException 고객 ID가 1 미만이거나 null인 경우 (INVALID_CUSTOMER_ID)
     * @throws ApiException 이미 탈퇴 처리된 고객인 경우 (INVALID_REQUEST)
     * @throws ApiException 고객이 존재하지 않는 경우 (CUSTOMER_NOT_FOUND)
     */
    @Transactional
    public CustomerDeleteResponse deleteCustomer(Long customerId) {

        validateCustomerId(customerId);

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    if (customerRepository.existsByIdIncludeDeleted(customerId) == 1) {
                        return new ApiException(ErrorCode.INVALID_REQUEST, "이미 탈퇴 처리된 고객입니다.");
                    }
                    return new ApiException(ErrorCode.CUSTOMER_NOT_FOUND);
                });

        customerRepository.delete(customer);

        return CustomerDeleteResponse.of(customerId);
    }

    /**
     * 고객 ID 유효성을 검증합니다.
     *
     * @param customerId 검증할 고객 ID
     * @throws ApiException 고객 ID가 1 미만이거나 null인 경우 (INVALID_CUSTOMER_ID)
     */
    private void validateCustomerId(Long customerId) {
        if (customerId == null || customerId < 1) {
            throw new ApiException(ErrorCode.INVALID_CUSTOMER_ID);
        }
    }
}
