package com.sparta.cch.backofficeproject.product.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.sparta.cch.backofficeproject.product.entity.Product;
import com.sparta.cch.backofficeproject.product.enums.ProductCategory;
import com.sparta.cch.backofficeproject.product.enums.ProductStatus;
import lombok.RequiredArgsConstructor;
import java.util.List;

import static com.sparta.cch.backofficeproject.product.entity.QProduct.product;
import static com.sparta.cch.backofficeproject.admin.entity.QAdmin.admin;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Product> findProductsWithFilters(String keyword, ProductCategory category, ProductStatus status, Pageable pageable) {

        // 1. 데이터 조회 (N+1 방지를 위해 fetchJoin 적용)
        List<Product> content = queryFactory
                .selectFrom(product)
                .leftJoin(product.admin, admin).fetchJoin()
                .where(
                        containsKeyword(keyword),
                        eqCategory(category),
                        eqStatus(status)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifier(pageable.getSort()))
                .fetch();

        // 2. 카운트 쿼리 (전체 개수)
        JPAQuery<Long> countQuery = queryFactory
                .select(product.count())
                .from(product)
                .where(
                        containsKeyword(keyword),
                        eqCategory(category),
                        eqStatus(status)
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression containsKeyword(String keyword) {
        return (keyword != null && !keyword.isBlank()) ? product.name.containsIgnoreCase(keyword) : null;
    }

    private BooleanExpression eqCategory(ProductCategory category) {
        return category != null ? product.category.eq(category) : null;
    }

    private BooleanExpression eqStatus(ProductStatus status) {
        return status != null ? product.status.eq(status) : null;
    }

    private OrderSpecifier<?> getOrderSpecifier(Sort sort) {
        for (Sort.Order order : sort) {
            // 여기서의 Order는 QueryDSL의 Order
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            switch (order.getProperty()) {
                case "price" -> { return new OrderSpecifier<>(direction, product.price); }
                case "stock" -> { return new OrderSpecifier<>(direction, product.stock); }
                default -> { return new OrderSpecifier<>(direction, product.createdAt); }
            }
        }
        return new OrderSpecifier<>(Order.DESC, product.createdAt);
    }
}