package com.sparta.cch.backofficeproject.product.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageInfo {
    private int currentPage;
    private int pageSize;
    private long totalElements;
    private int totalPages;
}