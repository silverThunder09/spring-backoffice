package com.sparta.cch.backofficeproject.order.service;

import com.sparta.cch.backofficeproject.admin.entity.Admin;
import com.sparta.cch.backofficeproject.admin.repository.AdminRepository;
import com.sparta.cch.backofficeproject.common.exception.ApiException;
import com.sparta.cch.backofficeproject.common.exception.ErrorCode;
import com.sparta.cch.backofficeproject.common.session.SessionConst;
import com.sparta.cch.backofficeproject.customer.entity.Customer;
import com.sparta.cch.backofficeproject.customer.repository.CustomerRepository;
import com.sparta.cch.backofficeproject.order.dto.*;
import com.sparta.cch.backofficeproject.order.entity.Order;
import com.sparta.cch.backofficeproject.order.entity.OrderStatus;
import com.sparta.cch.backofficeproject.order.repository.OrderRepository;
import com.sparta.cch.backofficeproject.product.entity.Product;
import com.sparta.cch.backofficeproject.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.sparta.cch.backofficeproject.product.enums.ProductStatus;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
     * 주문 목록을 조회합니다
     * <p>
     * 로그인한 관리자가 주문 목록을 조회하며,
     * 검색어, 상태값, 페이지네이션, 정렬 조건을 함께 처리합니다.
     *
     * @param adminId    세션에 저장된 로그인 관리자 ID
     * @param requestDto 주문 목록 조회 조건 DTO
     * @return 주문 목록 조회 결과 응답
     */
    public OrderListResponseDto getOrders(Long adminId, OrderListSearchRequestDto requestDto) {

        if (adminId == null || adminId < 1) {
            throw new ApiException(ErrorCode.UNAUTHORIZED);
        }

        OrderStatus orderStatus = null;

        /**
         * 사용자가 status를 보내면, 그 값이 진짜 주문 상태인지 확인
         */
        if (requestDto.getStatus() != null && !requestDto.getStatus().isBlank()) {
            try {
                orderStatus = OrderStatus.valueOf(requestDto.getStatus());
            } catch (IllegalArgumentException exception) {
                throw new ApiException(ErrorCode.INVALID_ORDER_STATUS);
            }
        }

        if (!requestDto.getSortBy().equals("quantity")
                && !requestDto.getSortBy().equals("totalPrice")
                && !requestDto.getSortBy().equals("orderedAt")) {
            throw new ApiException(ErrorCode.INVALID_SORT_BY);
        }

        if (!requestDto.getDirection().equals("asc")
                && !requestDto.getDirection().equals("desc")) {
            throw new ApiException(ErrorCode.INVALID_DIRECTION);
        }

        if (requestDto.getPage() < 1) {
            throw new ApiException(ErrorCode.INVALID_PAGE_NUMBER);
        }

        int page = requestDto.getPage() - 1;

        String sortProperty = requestDto.getSortBy();

        if (sortProperty.equals("orderedAt")) {
            sortProperty = "createdAt";
        }

        Sort.Direction direction = requestDto.getDirection().equals("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, sortProperty);

        Pageable pageable = PageRequest.of(page, requestDto.getSize(), sort);

        Page<Order> orderPage = orderRepository.findOrders(
                requestDto.getKeyword(),
                orderStatus,
                pageable
        );

        List<OrderListItemResponseDto> orders = orderPage.getContent().stream()
                .map(order -> OrderListItemResponseDto.builder()
                        .id(order.getId())
                        .orderNo(order.getOrderNo())
                        .customerName(order.getCustomer().getName())
                        .productName(order.getProduct().getName())
                        .quantity(order.getQuantity())
                        .totalPrice(order.getTotalPrice())
                        .status(order.getStatus())
                        .orderedAt(order.getOrderedAt())
                        .adminName(order.getAdmin() != null ? order.getAdmin().getName() : null)
                        .build())
                .toList();

        OrderPageInfoResponseDto pageInfo = OrderPageInfoResponseDto.builder()
                .currentPage(requestDto.getPage())
                .pageSize(requestDto.getSize())
                .totalElements(orderPage.getTotalElements())
                .totalPages(orderPage.getTotalPages())
                .build();

        return OrderListResponseDto.builder()
                .orders(orders)
                .pageInfo(pageInfo)
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
