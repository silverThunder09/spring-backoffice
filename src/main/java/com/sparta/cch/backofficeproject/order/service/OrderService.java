package com.sparta.cch.backofficeproject.order.service;

import com.sparta.cch.backofficeproject.admin.entity.Admin;
import com.sparta.cch.backofficeproject.admin.repository.AdminRepository;
import com.sparta.cch.backofficeproject.common.exception.ApiException;
import com.sparta.cch.backofficeproject.common.exception.ErrorCode;
import com.sparta.cch.backofficeproject.customer.entity.Customer;
import com.sparta.cch.backofficeproject.customer.repository.CustomerRepository;
import com.sparta.cch.backofficeproject.order.dto.OrderCreateRequestDto;
import com.sparta.cch.backofficeproject.order.dto.OrderCreateResponseDto;
import com.sparta.cch.backofficeproject.order.entity.Order;
import com.sparta.cch.backofficeproject.order.entity.OrderStatus;
import com.sparta.cch.backofficeproject.order.repository.OrderRepository;
import com.sparta.cch.backofficeproject.product.entity.Product;
import com.sparta.cch.backofficeproject.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.sparta.cch.backofficeproject.product.enums.ProductStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 주문 관련 비즈니스 로직을 처리하는 서비스 클래스
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;
    private final ProductRepository productRepository;

    /**
     * 주문을 생성합니다.
     * <p>
     * 현재는
     * Product, Customer, Admin 연관 로직은 다음 단계에서 채웁니다.
     */
    public OrderCreateResponseDto createOrder(OrderCreateRequestDto requestDto, Long adminId) {

        if (adminId == null || adminId < 1) {
            throw new ApiException(ErrorCode.INVALID_ADMIN_ID);
        }

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ApiException(ErrorCode.ADMIN_NOT_FOUND));

        Customer customer = customerRepository.findById(requestDto.getCustomerId())
                .orElseThrow(() -> new ApiException(ErrorCode.CUSTOMER_NOT_FOUND));

        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));

        if (product.getStatus() == ProductStatus.SOLD_OUT) {
            throw new ApiException(ErrorCode.PRODUCT_SOLD_OUT);
        }

        if (product.getStatus() == ProductStatus.DISCONTINUED) {
            throw new ApiException(ErrorCode.PRODUCT_DISCONTINUED);
        }

        if (requestDto.getQuantity() < 1) {
            throw new ApiException(ErrorCode.INVALID_QUANTITY);
        }

        if (product.getStock() < requestDto.getQuantity()) {
            throw new ApiException(ErrorCode.INSUFFICIENT_STOCK);
        }

        String orderNo = generateUniqueOrderNo();

        Integer orderPrice = product.getPrice();
        Integer totalPrice = orderPrice * requestDto.getQuantity();

        Order order = new Order(
                product,
                customer,
                admin,
                orderNo,
                orderPrice,
                requestDto.getQuantity(),
                totalPrice,
                OrderStatus.PENDING
        );

        Order savedOrder = orderRepository.save(order);

        return OrderCreateResponseDto.builder()
                .orderId(savedOrder.getId())
                .orderNo(savedOrder.getOrderNo())
                .customerId(savedOrder.getCustomer().getId())
                .productId(savedOrder.getProduct().getId())
                .quantity(savedOrder.getQuantity())
                .orderPrice(savedOrder.getOrderPrice())
                .totalPrice(savedOrder.getTotalPrice())
                .status(savedOrder.getStatus())
                .orderedAt(savedOrder.getOrderedAt())
                .build();
    }

    /**
     * 주문번호를 생성합니다.
     * 예: ORD-20260427-153000
     */
    private String generateUniqueOrderNo() {
        String orderNo;

        do {
            String currentTime = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));

            orderNo = "ORD-" +  currentTime;
        } while (orderRepository.existsByOrderNo(orderNo));

        return orderNo;
    }
}
