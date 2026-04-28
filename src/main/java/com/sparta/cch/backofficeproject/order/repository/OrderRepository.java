package com.sparta.cch.backofficeproject.order.repository;

import com.sparta.cch.backofficeproject.order.entity.Order;
import com.sparta.cch.backofficeproject.order.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 주문 엔티티에 접근하는 Repository 인터페이스
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByProductIdAndStatusIn(Long productId, List<OrderStatus> statuses);

    /**
     * 주문번호로 주문을 조회합니다.
     */
    Optional<Order> findByOrderNo(String orderNo);

    /**
     * 주문번호 중복 여부를 확인합니다.
     */
    boolean existsByOrderNo(String orderNo);

    /**
     * 주문 목록을 검색 조건에 따라 조회합니다.
     *
     * 주문번호 또는 고객명으로 검색할 수 있고,
     * 주문 상태로 필터링할 수 있습니다.
     *
     * @param keyword 주문번호 또는 고객명 검색어
     * @param status 주문 상태
     * @param pageable 페이징 및 정렬 정보
     * @return 주문 목록 페이지
     */
    @Query("""
    SELECT o
    FROM Order o
    WHERE (:status IS NULL OR o.status = :status)
      AND (
            :keyword IS NULL
            OR :keyword = ''
            OR LOWER(o.orderNo) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(o.customer.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
          )
    """)
    Page<Order> findOrders(
            @Param("keyword") String keyword,
            @Param("status") OrderStatus status,
            Pageable pageable
    );
}