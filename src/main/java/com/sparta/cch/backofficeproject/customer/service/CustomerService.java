package com.sparta.cch.backofficeproject.customer.service;

import com.sparta.cch.backofficeproject.admin.dto.AdminApiResponse;
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
}
