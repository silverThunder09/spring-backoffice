package com.sparta.cch.backofficeproject.common.session;

public class SessionAdmin {

    private final Long adminId;
    private final String email;
    private final String role;

    public SessionAdmin(Long adminId, String email, String role) {
        this.adminId = adminId;
        this.email = email;
        this.role = role;
    }

    public Long getAdminId() {
        return adminId;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
