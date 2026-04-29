package com.sparta.cch.backofficeproject.product.controller;

import com.sparta.cch.backofficeproject.common.exception.ApiException;
import com.sparta.cch.backofficeproject.common.exception.ErrorCode;
import com.sparta.cch.backofficeproject.common.session.SessionConst;
import com.sparta.cch.backofficeproject.product.dto.*;
import com.sparta.cch.backofficeproject.product.enums.ProductCategory;
import com.sparta.cch.backofficeproject.product.enums.ProductStatus;
import com.sparta.cch.backofficeproject.product.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

    @GetMapping("/{productId}")
    public ResponseEntity<ProductGetDetailResponse> getDetailProduct(
        // 단건으로 조회할 id를 가져옵니다.
        @PathVariable("productId") Long productId) {

        // Service Logic을 호출하여 상품 정보를 가져옵니다.
        ProductGetDetailResponse response = productService.getDetailProduct(productId);

        // 결과 반환합니다.
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<ProductPageResponse> getProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "페이지 번호는 1 이상이어야 합니다.") int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder,
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) ProductStatus status
    ) {
        if (keyword != null && keyword.length() < 2) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "검색어는 2자 이상이어야 합니다.");
        }

        ProductPageResponse response = productService.getProducts(
                keyword, page, size, sortBy, sortOrder, category, status
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductUpdateResponse> updateProduct(
            // 단건으로 조회할 id를 가져옵니다.
            @PathVariable("productId") Long productId,
            @SessionAttribute(name = SessionConst.ADMIN_ID) Long adminId,
            @Valid @RequestBody ProductUpdateRequest request) {

        // Service Login을 호출하여 상품 정보를 가져옵니다.
        ProductUpdateResponse response = productService.updateProduct(productId, request);

        // 결과 반환합니다.
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{productId}/stock")
    public ResponseEntity<ProductStockUpdateResponse> updateProductStock(
            // 단건으로 조회할 id를 가져옵니다.
            @PathVariable("productId") Long productId,
            @SessionAttribute(name = SessionConst.ADMIN_ID) Long adminId,
            @Valid @RequestBody ProductStockUpdateRequest request) {

        // Service Login을 호출하여 상품 정보를 가져옵니다.
        ProductStockUpdateResponse response = productService.updateProductStock(productId, request);

        // 결과 반환합니다.
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{productId}/status")
    public ResponseEntity<ProductStatusUpdateResponse> updateProductStatus(
            // 단건으로 조회할 id를 가져옵니다.
            @PathVariable("productId") Long productId,
            @SessionAttribute(name = SessionConst.ADMIN_ID) Long adminId,
            @Valid @RequestBody ProductStatusUpdateRequest request) {

        // Service Logic을 호출하여 상품 정보를 가져옵니다.
        ProductStatusUpdateResponse response = productService.updateProductStatus(productId, request);

        // 결과 반환합니다.
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ProductDeleteResponse> deleteProduct(
            // 단건으로 조히할 id를 가져옵니다.
            @PathVariable("productId") Long productId,
            @SessionAttribute(name = SessionConst.ADMIN_ID) Long adminId) {
        // Service Logic을 호출하여 상품을 삭제합니다.
        ProductDeleteResponse response = productService.deleteProduct(productId);

        // 결과 반환합니다.
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
