package com.sparta.cch.backofficeproject.customer.repository;

import com.sparta.cch.backofficeproject.customer.entity.Customer;
import com.sparta.cch.backofficeproject.customer.entity.CustomerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByEmail(String email);

    /**
     * 검색 조건에 맞는 고객 목록을 조회합니다.
     *
     * <p>keyword는 이름 또는 이메일에 대한 부분 일치 검색에 사용하며,
     * status가 null이면 상태 조건은 적용하지 않습니다.
     * pageable을 통해 페이징과 정렬을 함께 처리합니다.</p>
     *
     * @param keyword 이름 또는 이메일 검색 키워드
     * @param status 고객 상태 필터
     * @param pageable 페이징 및 정렬 정보
     * @return 검색 조건이 반영된 고객 목록 페이지
     */
    @Query("""
            SELECT c 
            FROM Customer c
            WHERE (:keyword IS NULL
                   OR lower(c.name) LIKE lower(concat('%', :keyword, '%'))
                   OR lower(c.email) LIKE lower(concat('%', :keyword, '%')))
              AND (:status IS NULL OR c.status = :status)
            """)
    Page<Customer> searchCustomers(
            @Param("keyword") String keyword,
            @Param("status") CustomerStatus status,
            Pageable pageable
    );

    @Query(value = "SELECT EXISTS(SELECT 1 FROM customers WHERE id = :customerId)", nativeQuery = true)
    int existsByIdIncludeDeleted(@Param("customerId") Long customerId);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM customers WHERE email = :email)", nativeQuery = true)
    int existsByEmailIncludeDeleted(String email);
}

