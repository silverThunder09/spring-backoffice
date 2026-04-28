package com.sparta.cch.backofficeproject.order.controller;

import com.sparta.cch.backofficeproject.common.session.SessionConst;
import com.sparta.cch.backofficeproject.order.dto.*;
import com.sparta.cch.backofficeproject.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 주문 관련 요청을 처리하는 컨트롤러 클래스
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * 주문 생성 API
     */
    @PostMapping
    public ResponseEntity<OrderApiResponse<OrderCreateResponseDto>> createOrder(
            @SessionAttribute(name = SessionConst.ADMIN_ID) Long adminId,
            @Valid @RequestBody OrderCreateRequestDto requestDto
    ) {
        OrderCreateResponseDto responseDto = orderService.createOrder(requestDto, adminId);

        OrderApiResponse<OrderCreateResponseDto> response = OrderApiResponse.success(
                HttpStatus.CREATED.value(),
                "주문 생성 성공",
                responseDto
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 주문 목록 조회 API
     *
     * 로그인한 관리자가 주문 목록을 조회합니다.
     * 주문번호 또는 고객명으로 검색할 수 있고,
     * 주문 상태 필터, 페이지네이션, 정렬 조건을 함께 받을 수 있습니다.
     *
     * @param adminId    세션에 저장된 로그인 관리자 ID
     * @param requestDto 주문 목록 조회 조건 DTO
     * @return 주문 목록 조회 결과 응답
     */
    @GetMapping
    public ResponseEntity<OrderApiResponse<OrderListResponseDto>> getOrders(
            @SessionAttribute(name = SessionConst.ADMIN_ID) Long adminId,
            @ModelAttribute OrderListSearchRequestDto requestDto // 쿼리 파라미터를 받는 API
    ) {
        OrderListResponseDto responseDto = orderService.getOrders(adminId, requestDto);

        OrderApiResponse<OrderListResponseDto> response = OrderApiResponse.success(
                HttpStatus.OK.value(),
                "주문 목록 조회 성공",
                responseDto
        );

        return ResponseEntity.ok(response);
    }

    /**
     * 주문 상세 조회 API
     *
     * 로그인한 관리자가 특정 주문의 상세 정보를 조회합니다.
     *
     * @param adminId 세션에 저장된 로그인 관리자 ID
     * @param orderId 조회할 주문 ID
     * @return 주문 상세 조회 결과 응답
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderApiResponse<OrderDetailResponseDto>> getOrder(
            @SessionAttribute(name = SessionConst.ADMIN_ID) Long adminId,
            @PathVariable Long orderId
    ) {
        OrderDetailResponseDto responseDto = orderService.getOrder(adminId, orderId);

        OrderApiResponse<OrderDetailResponseDto> response = OrderApiResponse.success(
                HttpStatus.OK.value(),
                "주문 상세 조회 성공",
                responseDto
        );

        return ResponseEntity.ok(response);
    }
}
