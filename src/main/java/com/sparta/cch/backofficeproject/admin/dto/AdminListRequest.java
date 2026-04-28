package com.sparta.cch.backofficeproject.admin.dto;

import com.sparta.cch.backofficeproject.admin.entity.AdminRole;
import com.sparta.cch.backofficeproject.admin.entity.AdminStatus;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminListRequest {

    @Size(max = 100, message = "검색 키워드는 100자 이하여야 합니다.")
    private String keyword;

    @NotNull(message = "페이지 번호는 필수입니다.")
    @Min(value = 1, message = "페이지 번호는 1 이상이어야 합니다.")
    private Integer page = 1;

    @NotNull(message = "페이지 크기는 필수입니다.")
    @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
    @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다.")
    private Integer size = 10;

    @Pattern(regexp = "^(createdAt|name|email)$", message = "정렬 기준이 올바르지 않습니다.")
    private String sortBy = "createdAt";

    @Pattern(regexp = "(?i)^(asc|desc)$", message = "정렬 순서가 올바르지 않습니다.")
    private String direction = "desc";

    private AdminRole role;

    private AdminStatus status;
}

