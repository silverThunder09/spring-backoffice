package com.sparta.cch.backofficeproject.product.controller;

import com.sparta.cch.backofficeproject.common.dto.CommonResponse;
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

    /**
     * 신규 상품을 등록합니다.
     *
     * @param adminId 현재 로그인한 관리자 ID (세션)
     * @param request 상품 등록 요청 정보
     * @return 등록된 상품 정보
     */
    @PostMapping
    public ResponseEntity<CommonResponse<ProductCreateResponse>> createProduct(
            @SessionAttribute(name = SessionConst.ADMIN_ID, required = false) Long adminId,
            @Valid @RequestBody ProductCreateRequest request) {

        ProductCreateResponse response = productService.createProduct(adminId, request);

        CommonResponse<ProductCreateResponse> data = CommonResponse.success(
                HttpStatus.CREATED.value(),
                "상품 추가에 성공했습니다.",
                response
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(data);
    }

    /**
     * 특정 상품의 상세 정보를 조회합니다.
     *
     * @param productId 조회할 상품 ID
     * @return 상품 상세 정보
     */
    @GetMapping("/{productId}")
    public ResponseEntity<CommonResponse<ProductGetDetailResponse>> getDetailProduct(
            @PathVariable("productId") Long productId) {

        ProductGetDetailResponse response = productService.getDetailProduct(productId);

        CommonResponse<ProductGetDetailResponse> data = CommonResponse.success(
                HttpStatus.OK.value(),
                "상품 상세 조회에 성공했습니다.",
                response
        );

        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    /**
     * 상품 목록을 조회합니다.
     * 검색 키워드, 페이징, 정렬, 카테고리/상태 필터링을 지원합니다.
     *
     * @param keyword 검색어
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @param sortBy 정렬 기준
     * @param sortOrder 정렬 방향
     * @param category 카테고리 필터
     * @param status 상태 필터
     * @return 상품 목록 및 페이징 정보
     */
    @GetMapping
    public ResponseEntity<CommonResponse<ProductPageResponse>> getProducts(
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

        CommonResponse<ProductPageResponse> data = CommonResponse.success(
                HttpStatus.OK.value(),
                "상품 목록 조회에 성공했습니다.",
                response
        );

        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    /**
     * 특정 상품의 기본 정보를 수정합니다.
     *
     * @param productId 수정할 상품 ID
     * @param adminId 현재 로그인한 관리자 ID (세션)
     * @param request 수정할 상품 정보
     * @return 수정된 상품 정보
     */
    @PutMapping("/{productId}")
    public ResponseEntity<CommonResponse<ProductUpdateResponse>> updateProduct(
            @PathVariable("productId") Long productId,
            @SessionAttribute(name = SessionConst.ADMIN_ID) Long adminId,
            @Valid @RequestBody ProductUpdateRequest request) {

        ProductUpdateResponse response = productService.updateProduct(productId, request);

        CommonResponse<ProductUpdateResponse> data = CommonResponse.success(
                HttpStatus.OK.value(),
                "상품 정보 수정에 성공했습니다.",
                response
        );

        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    /**
     * 특정 상품의 재고를 변경합니다.
     *
     * @param productId 재고를 변경할 상품 ID
     * @param adminId 현재 로그인한 관리자 ID (세션)
     * @param request 변경할 재고 정보
     * @return 변경된 상품 재고 정보
     */
    @PatchMapping("/{productId}/stock")
    public ResponseEntity<CommonResponse<ProductStockUpdateResponse>> updateProductStock(
            @PathVariable("productId") Long productId,
            @SessionAttribute(name = SessionConst.ADMIN_ID) Long adminId,
            @Valid @RequestBody ProductStockUpdateRequest request) {

        ProductStockUpdateResponse response = productService.updateProductStock(productId, request);

        CommonResponse<ProductStockUpdateResponse> data = CommonResponse.success(
                HttpStatus.OK.value(),
                "상품 재고 수정에 성공했습니다.",
                response
        );

        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    /**
     * 특정 상품의 상태를 변경합니다.
     *
     * @param productId 상태를 변경할 상품 ID
     * @param adminId 현재 로그인한 관리자 ID (세션)
     * @param request 변경할 상태 정보
     * @return 변경된 상품 상태 정보
     */
    @PatchMapping("/{productId}/status")
    public ResponseEntity<CommonResponse<ProductStatusUpdateResponse>> updateProductStatus(
            @PathVariable("productId") Long productId,
            @SessionAttribute(name = SessionConst.ADMIN_ID) Long adminId,
            @Valid @RequestBody ProductStatusUpdateRequest request) {

        ProductStatusUpdateResponse response = productService.updateProductStatus(productId, request);

        CommonResponse<ProductStatusUpdateResponse> data = CommonResponse.success(
                HttpStatus.OK.value(),
                "상품 상태 변경에 성공했습니다.",
                response
        );

        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    /**
     * 특정 상품을 삭제합니다.
     *
     * @param productId 삭제할 상품 ID
     * @param adminId 현재 로그인한 관리자 ID (세션)
     * @return 삭제된 상품 ID 정보
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<CommonResponse<ProductDeleteResponse>> deleteProduct(
            @PathVariable("productId") Long productId,
            @SessionAttribute(name = SessionConst.ADMIN_ID) Long adminId) {

        ProductDeleteResponse response = productService.deleteProduct(productId);

        CommonResponse<ProductDeleteResponse> data = CommonResponse.success(
                HttpStatus.OK.value(),
                "상품 삭제에 성공했습니다.",
                response
        );

        return ResponseEntity.status(HttpStatus.OK).body(data);
    }
}