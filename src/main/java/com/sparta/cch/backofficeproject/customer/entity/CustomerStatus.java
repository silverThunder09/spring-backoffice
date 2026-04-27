package com.sparta.cch.backofficeproject.customer.entity;

public enum CustomerStatus {
    ACTIVE("활성"),
    INACTIVE("비활성"),
    SUSPENDED("정지");

    private final String label;

    CustomerStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

