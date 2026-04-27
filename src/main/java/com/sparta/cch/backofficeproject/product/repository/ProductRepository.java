package com.sparta.cch.backofficeproject.product.repository;

import com.sparta.cch.backofficeproject.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
