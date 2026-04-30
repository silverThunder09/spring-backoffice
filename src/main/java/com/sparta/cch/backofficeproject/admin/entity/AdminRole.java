package com.sparta.cch.backofficeproject.admin.entity;

import java.util.Arrays;

public enum AdminRole {

    SUPER("슈퍼 관리자"),
    OPERATION("운영 관리자"),
    CS("CS 관리자");

    private final String label;

    AdminRole(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /**
     * 역할 문자열을 AdminRole enum으로 변환합니다.
     *
     * <p>입력값의 앞뒤 공백을 제거하고 대문자로 정규화한 뒤,
     * 일치하는 관리자 역할 enum을 반환합니다.</p>
     *
     * @param roleValue 변환할 역할 문자열
     * @return 변환된 관리자 역할 enum
     * @throws IllegalArgumentException 일치하는 관리자 역할이 없는 경우
     */
    public static AdminRole from(String roleValue) {
        String normalizedRole = roleValue.trim().toUpperCase();

        return Arrays.stream(values())
                .filter(role -> role.name().equals(normalizedRole))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid role: " + roleValue));
    }
}
