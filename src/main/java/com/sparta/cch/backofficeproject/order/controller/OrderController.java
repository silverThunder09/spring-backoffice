package com.sparta.cch.backofficeproject.order.controller;

import com.sparta.cch.backofficeproject.common.session.SessionConst;
import com.sparta.cch.backofficeproject.order.dto.OrderApiResponse;
import com.sparta.cch.backofficeproject.order.dto.OrderCreateRequestDto;
import com.sparta.cch.backofficeproject.order.dto.OrderCreateResponseDto;
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


}
