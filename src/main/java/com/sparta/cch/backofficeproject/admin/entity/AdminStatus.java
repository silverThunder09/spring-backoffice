package com.sparta.cch.backofficeproject.admin.entity;

public enum AdminStatus {
    PENDING("승인 대기"),
    ACTIVE("승인"),
    REJECTED("거부"),
    INACTIVE("비활성화"),
    SUSPENDED("정지");

    private final String label;
    AdminStatus(String label) {
        this.label = label;
    }


    public String getLabel() {
        return label;
    }

}
