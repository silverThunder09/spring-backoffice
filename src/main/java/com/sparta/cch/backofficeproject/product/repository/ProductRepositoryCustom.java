package com.sparta.cch.backofficeproject.product.repository;

import com.sparta.cch.backofficeproject.product.entity.Product;
import com.sparta.cch.backofficeproject.product.enums.ProductCategory;
import com.sparta.cch.backofficeproject.product.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<Product> findProductsWithFilters(String keyword, ProductCategory category, ProductStatus status, Pageable pageable);
}