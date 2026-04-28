package com.sparta.cch.backofficeproject.admin.dto;

import com.sparta.cch.backofficeproject.admin.entity.Admin;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class AdminListResponse {

    private final List<AdminListItemResponse> admins;
    private final int page;
    private final int size;
    private final long totalCount;
    private final int totalPages;

    @Builder
    public AdminListResponse(List<AdminListItemResponse> admins, int page, int size, long totalCount, int totalPages) {
        this.admins = admins;
        this.page = page;
        this.size = size;
        this.totalCount = totalCount;
        this.totalPages = totalPages;
    }

    public static AdminListResponse of(Page<Admin> adminPage) {

        List<AdminListItemResponse> admins = adminPage.getContent().stream()
                .map(AdminListItemResponse::of)
                .toList();

        return AdminListResponse.builder()
                .admins(admins)
                .page(adminPage.getNumber() + 1)
                .size(adminPage.getSize())
                .totalCount(adminPage.getTotalElements())
                .totalPages(adminPage.getTotalPages())
                .build();
    }
}
