package com.sparta.cch.backofficeproject.customer.service;

import com.sparta.cch.backofficeproject.admin.dto.AdminApiResponse;
import com.sparta.cch.backofficeproject.admin.entity.Admin;
import com.sparta.cch.backofficeproject.common.exception.ApiException;
import com.sparta.cch.backofficeproject.common.exception.ErrorCode;
import com.sparta.cch.backofficeproject.customer.dto.CustomerDetailResponse;
import com.sparta.cch.backofficeproject.customer.dto.CustomerListRequest;
import com.sparta.cch.backofficeproject.customer.dto.CustomerListResponse;
import com.sparta.cch.backofficeproject.customer.entity.Customer;
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
    public AdminApiResponse<CustomerListResponse> getAll(CustomerListRequest request) {

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

        CustomerListResponse data = CustomerListResponse.of(customerPage);

        return AdminApiResponse.success(
                200,
                "고객 목록 조회에 성공했습니다.",
                data
        );
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
    public AdminApiResponse<CustomerDetailResponse> getCustomer(Long customerId) {

        Customer customer = findCustomerById(customerId);

        CustomerDetailResponse data = CustomerDetailResponse.of(customer);

        return AdminApiResponse.success(
                200,
                "고객 상세 조회에 성공했습니다.",
                data
        );
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

}
