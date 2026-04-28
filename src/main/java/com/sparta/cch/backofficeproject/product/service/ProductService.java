package com.sparta.cch.backofficeproject.product.service;

import com.sparta.cch.backofficeproject.admin.entity.Admin;
import com.sparta.cch.backofficeproject.admin.repository.AdminRepository;
import com.sparta.cch.backofficeproject.common.exception.ApiException;
import com.sparta.cch.backofficeproject.common.exception.ErrorCode;
import com.sparta.cch.backofficeproject.product.dto.*;
import com.sparta.cch.backofficeproject.product.entity.Product;
import com.sparta.cch.backofficeproject.product.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final AdminRepository adminRepository;

    /**
     * 새로운 상품을 등록합니다.
     * @param adminId 세션에서 전달받은 관리자 고유 식별자
     * @param request 상품명, 가격, 재고 등 사용자가 입력한 상품 정보 DTO
     * @return 등록된 상품의 상세 정보를 포함한 응답 DTO
     * @throws ApiException 해당 ID의 관리자가 존재하지 않을 경우 ADMIN_NOT_FOUND 예외 발생
     */
    @Transactional
    public ProductCreateResponse createProduct(Long adminId, ProductCreateRequest request) {
        log.info(">>> 상품 등록 서비스 진입 - adminId: {}", adminId);
        // 관리자 존재 여부 확인 (없으면 예외 발생)
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ApiException(ErrorCode.ADMIN_NOT_FOUND));

        log.info(">>> 관리자 확인 완료: {}", admin.getName());

        // 뽑아낸 객체를 toEntity에 넣어줌.
        Product product = request.toEntity(admin);

        // DB에 저장
        Product savedProduct = productRepository.save(product);
        log.info(">>> 상품 저장 성공: {}", savedProduct.getName());

        // 저장한 결과를 Response DTO로 바꿔서 반환
        return ProductCreateResponse.of(savedProduct);
    }

    /**
     * 상품을 상세(단건) 조회합니다.
     * @param productId 조회할 상품 ID(고유 식별자)
     * @return 상품 상세 정보 DTO
     * @throws ApiException 해당 상품의 ID가 존재하지 않을 경우 PRODUCT_NOT_FOUND 예외 발생
     */
    @Transactional(readOnly = true)
    public ProductGetDetailResponse getDetailProduct(Long productId) {
        // 상품 단건 조회 (DB에 없으면 404 Not Found 반환)
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
        return ProductGetDetailResponse.of(product);
    }

    /**
     * 상품 기본 정보를 수정합니다.
     * 관리자 권한을 가진 사용자라면 누구나 상품의 이름, 카테고리, 가격, 설명을 수정할 수 있습니다.
     * @param productId 수정할 상품의 고유 식별자 (PK)
     * @param request   클라이언트로부터 전달받은 수정 데이터 (DTO)
     * @return ProductUpdateResponse 수정이 완료된 상품 정보와 수정 일시
     * @throws ApiException 해당 ID의 상품이 존재하지 않을 경우 PRODUCT_NOT_FOUND(404) 발생
     */
    @Transactional
    public ProductUpdateResponse updateProduct(Long productId, ProductUpdateRequest request) {
        // 1. DB에서 수정할 상품 조회
        // 해당하는 상품이 없으면 즉시 예외를 발생시켜 컨트롤러로 던짐.
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));

        // 2. 엔티티 수정 (JPA 더티 체킹)
        // Transaction 안에서 Entity의 값을 변경하면,
        // 메서드가 끝날 때 스프링이 알아서 변경된 부분을 감지하고 DB에 UPDATE 쿼리를 날려줌.
        product.update(
                request.getName(),
                request.getCategory(),
                request.getPrice(),
                request.getDescription()
        );

        // 3. 수정된 Entity를 응답 DTO로 매핑하여 반환함.
        return ProductUpdateResponse.of(product);
    }

    /**
     * 상품 재고 변경 및 상태를 자동으로 갱신합니다.
     * - 재고가 0 이하가 될 경우 : 상태를 품절(SOLD_OUT)로 자동 전환
     * - 재고가 1 이상이 될 경우 : 상태를 판매중(SALE)으로 자동 전환
     * - 단, 현재 상품 상태가 단종(DISCONTINUED)인 경우 상태 변경 로직은 무시됩니다.
     * @param productId 재고를 변경할 상품의 고유 식별자 (PK)
     * @param request   클라이언트로부터 전달받은 변경할 재고 수량 (DTO)
     * @return ProductStockUpdateResponse 갱신된 재고, 상태 및 수정 일시
     * @throws ApiException 해당 ID의 상품이 존재하지 않을 경우 PRODUCT_NOT_FOUND(404) 발생
     */
    @Transactional
    public ProductStockUpdateResponse updateProductStock(Long productId, ProductStockUpdateRequest request) {
        // 기존 상품 단건 조회 (없으면 404 Not Found 반환)
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
        // 재고 변경 및 상태 자동 갱신 (JPA Dirty Checking)
        product.updateStock(request.getStock());
        // 결과 반환
        return ProductStockUpdateResponse.of(product);
    }
}
