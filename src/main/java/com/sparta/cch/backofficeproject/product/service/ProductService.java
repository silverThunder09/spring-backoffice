package com.sparta.cch.backofficeproject.product.service;

import com.sparta.cch.backofficeproject.admin.entity.Admin;
import com.sparta.cch.backofficeproject.admin.repository.AdminRepository;
import com.sparta.cch.backofficeproject.common.exception.ApiException;
import com.sparta.cch.backofficeproject.common.exception.ErrorCode;
import com.sparta.cch.backofficeproject.product.dto.ProductCreateRequest;
import com.sparta.cch.backofficeproject.product.dto.ProductCreateResponse;
import com.sparta.cch.backofficeproject.product.entity.Product;
import com.sparta.cch.backofficeproject.product.repository.ProductRepository;
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
}
