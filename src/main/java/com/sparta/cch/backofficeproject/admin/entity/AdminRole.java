package com.sparta.cch.backofficeproject.admin.entity;

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

    public static AdminRole fromLabel(String label) {
        for (AdminRole role : values()) {
            if (role.getLabel().equals(label)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role label: " + label);
    }
}
