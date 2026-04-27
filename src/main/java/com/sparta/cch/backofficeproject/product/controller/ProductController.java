package com.sparta.cch.backofficeproject.product.controller;

import com.sparta.cch.backofficeproject.common.exception.ApiException;
import com.sparta.cch.backofficeproject.common.exception.ErrorCode;
import com.sparta.cch.backofficeproject.common.session.SessionAdmin;
import com.sparta.cch.backofficeproject.common.session.SessionConst;
import com.sparta.cch.backofficeproject.product.dto.ProductCreateRequest;
import com.sparta.cch.backofficeproject.product.dto.ProductCreateResponse;
import com.sparta.cch.backofficeproject.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductCreateResponse> createProduct(
            // 세션에서 SessionAdmin 객체를 꺼냅니다.
            @SessionAttribute(name = SessionConst.ADMIN_ID, required = false) Long adminId,
            @Valid @RequestBody ProductCreateRequest request) {

        // productService의 createProduct를 통해 위에서 추출한 adminId, request 객체를 집어넣습니다.
        ProductCreateResponse response = productService.createProduct(adminId, request);

        // 결과 반환합니다.
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
