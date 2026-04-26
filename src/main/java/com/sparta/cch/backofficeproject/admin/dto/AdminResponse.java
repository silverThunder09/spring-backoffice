package com.sparta.cch.backofficeproject.admin.dto;

import com.sparta.cch.backofficeproject.admin.entity.Admin;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminResponse {

    private final int status;
    private final String message;
    private final Object data;

    public AdminResponse(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static AdminResponse createSignUpResponse(Admin admin) {
        SignUpData signUpData = new SignUpData(
                admin.getId(),
                admin.getName(),
                admin.getEmail(),
                admin.getPhoneNumber(),
                admin.getRole().name(),
                admin.getStatus().name(),
                admin.getCreatedAt(),
                admin.getUpdatedAt()
        );

        return new AdminResponse(
                201,
                "관리자 회원가입 신청이 완료되었습니다. 슈퍼 관리자의 승인을 기다려주세요.",
                signUpData
        );
    }

    public static AdminResponse createLoginResponse(Admin admin) {
        LoginData loginData = new LoginData(
                admin.getId(),
                admin.getEmail(),
                admin.getRole().name(),
                admin.getStatus().name()
        );

        return new AdminResponse(
                200,
                "로그인에 성공했습니다.",
                loginData
        );
    }

    public static AdminResponse createLogoutResponse() {
        return new AdminResponse(
                200,
                "로그아웃에 성공했습니다.",
                null
        );
    }

    @Getter
    public static class SignUpData {

        private final Long adminId;
        private final String name;
        private final String email;
        private final String phone;
        private final String role;
        private final String status;
        private final LocalDateTime createdAt;
        private final LocalDateTime updatedAt;

        public SignUpData(Long adminId, String name, String email, String phone, String role, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.adminId = adminId;
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.role = role;
            this.status = status;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }
    }

    @Getter
    public static class LoginData {

        private final Long adminId;
        private final String email;
        private final String role;
        private final String status;

        public LoginData(Long adminId, String email, String role, String status) {
            this.adminId = adminId;
            this.email = email;
            this.role = role;
            this.status = status;
        }
    }

}
