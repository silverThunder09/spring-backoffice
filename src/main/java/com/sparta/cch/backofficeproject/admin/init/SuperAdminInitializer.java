package com.sparta.cch.backofficeproject.admin.init;

import com.sparta.cch.backofficeproject.admin.entity.Admin;
import com.sparta.cch.backofficeproject.admin.entity.AdminRole;
import com.sparta.cch.backofficeproject.admin.repository.AdminRepository;
import com.sparta.cch.backofficeproject.common.config.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SuperAdminInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    // 초기 슈퍼 관리자 데이터 값
    @Value("${app.super-admin.name:슈퍼관리자}")
    private String superAdminName;

    @Value("${app.super-admin.email:admin@admin.com}")
    private String superAdminEmail;

    @Value("${app.super-admin.password:12345678}")
    private String superAdminPassword;

    @Value("${app.super-admin.phone:010-0000-0000}")
    private String superAdminPhone;

    /**
     * 애플리케이션 시작 시 슈퍼 관리자가 없으면 기본 슈퍼 관리자 계정을 생성합니다.
     *
     * @param args 애플리케이션 실행 인자
     */
    @Override
    @Transactional
    public void run(String... args) {
        if (adminRepository.existsByRole(AdminRole.SUPER)) {
            return;
        }

        Admin superAdmin = Admin.createSuperAdmin(
                superAdminName,
                superAdminEmail,
                superAdminPhone,
                passwordEncoder.encode(superAdminPassword)
        );

        adminRepository.save(superAdmin);
    }
}
