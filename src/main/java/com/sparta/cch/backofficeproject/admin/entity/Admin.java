package com.sparta.cch.backofficeproject.admin.entity;

import com.sparta.cch.backofficeproject.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "admins")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false, length = 200)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdminStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdminRole role;

    private LocalDateTime approvedAt;

    private LocalDateTime rejectedAt;

    private String rejectReason;

    public Admin(String name, String email, String phone, String password, AdminStatus status, AdminRole role) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.status = status;
        this.role = role;
    }

    public static Admin signUp(String name, String email, String phone, String password, AdminStatus status, AdminRole role) {
        return new Admin(name, email, phone, password, status, role);
    }

    public static Admin createSuperAdmin(String name, String email, String phone, String password) {
        Admin admin = new Admin(name, email, phone, password, AdminStatus.ACTIVE, AdminRole.SUPER);
        admin.approvedAt = LocalDateTime.now();
        return admin;
    }

    public void update(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public void updateStatus(AdminStatus status) {
        this.status = status;
    }

    public void updateRole(AdminRole role) {
        this.role = role;
    }

    public void approve() {
        this.status = AdminStatus.ACTIVE;
        this.approvedAt = LocalDateTime.now();
        this.rejectedAt = null;
        this.rejectReason = null;
    }

    public void reject(String rejectReason) {
        this.status = AdminStatus.REJECTED;
        this.approvedAt = LocalDateTime.now();
        this.rejectedAt = LocalDateTime.now();
        this.rejectReason = rejectReason;
    }

}
