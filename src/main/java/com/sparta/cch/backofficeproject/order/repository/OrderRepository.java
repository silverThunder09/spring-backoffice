package com.sparta.cch.backofficeproject.order.repository;

import com.sparta.cch.backofficeproject.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 주문 엔티티에 접근하는 Repository 인터페이스
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * 주문번호로 주문을 조회합니다.
     */
    Optional<Order> findByOrderNo(String orderNo);

    /**
     * 주문번호 중복 여부를 확인합니다.
     */
    boolean existsByOrderNo(String orderNo);
}
