package com.sparta.cch.backofficeproject.order.entity;

import com.sparta.cch.backofficeproject.admin.entity.Admin;
import com.sparta.cch.backofficeproject.common.entity.BaseEntity;
import com.sparta.cch.backofficeproject.customer.entity.Customer;
import com.sparta.cch.backofficeproject.product.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 주문 정보를 저장하는 엔티티 클래스
 * <p>
 * 주문은 고객 1명, 상품 1개와 연결됩니다.
 * 관리자(admin)는 CS 주문 등록 관리자 정보이므로 null일 수 있습니다.
 * <p>
 * BaseEntity의 createdAt -> ordered_at
 * BaseEntity의 updatedAt -> updated_at
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
@AttributeOverrides({
        @AttributeOverride(name = "createdAt", column = @Column(name = "ordered_at", updatable = false)),
        @AttributeOverride(name = "updatedAt", column = @Column(name = "updated_at"))
})
public class Order extends BaseEntity {

    /**
     * 주문 고유 식별자
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 주문한 상품
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * 주문한 고객
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    /**
     * 주문 등록 관리자
     * 고객 직접 주문이 아닌 CS 주문인 경우 연결될 수 있습니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    /**
     * 주문 번호
     */
    @Column(name = "order_no", nullable = false, unique = true, length = 100)
    private String orderNo;

    /**
     * 주문 당시 상품 가격
     */
    @Column(name = "order_price", nullable = false)
    private Integer orderPrice;

    /**
     * 주문 수량
     */
    @Column(nullable = false)
    private Integer quantity;

    /**
     * 주문 총 금액
     */
    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    /**
     * 주문 상태
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    /**
     * 주문 취소 사유
     */
    @Column(name = "cancel_reason", length = 200)
    private String cancelReason;

    /**
     * 주문 생성용 생성자
     */
    public Order(
            Product product,
            Customer customer,
            Admin admin,
            String orderNo,
            Integer orderPrice,
            Integer quantity,
            Integer totalPrice,
            OrderStatus status
    ) {
        this.product = product;
        this.customer = customer;
        this.admin = admin;
        this.orderNo = orderNo;
        this.orderPrice = orderPrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    /**
     * 주문 상태를 변경합니다.
     */
    public void changeStatus(OrderStatus status) {
        this.status = status;
    }

    /**
     * 주문을 취소합니다.
     */
    public void cancel(String cancelReason) {
        this.status = OrderStatus.CANCELED;
        this.cancelReason = cancelReason;
    }

    /**
     * 주문일시를 orderedAt 의미로 반환합니다.
     */
    public LocalDateTime getOrderedAt() {
        return getCreatedAt();
    }
}
