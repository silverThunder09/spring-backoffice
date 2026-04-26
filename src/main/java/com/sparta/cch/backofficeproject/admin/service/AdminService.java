package com.sparta.cch.backofficeproject.admin.service;

import com.sparta.cch.backofficeproject.admin.dto.AdminLoginRequest;
import com.sparta.cch.backofficeproject.admin.dto.AdminResponse;
import com.sparta.cch.backofficeproject.admin.dto.AdminSignUpRequest;
import com.sparta.cch.backofficeproject.admin.entity.Admin;
import com.sparta.cch.backofficeproject.admin.entity.AdminRole;
import com.sparta.cch.backofficeproject.admin.entity.AdminStatus;
import com.sparta.cch.backofficeproject.admin.repository.AdminRepository;
import com.sparta.cch.backofficeproject.common.config.PasswordEncoder;
import com.sparta.cch.backofficeproject.common.exception.ApiException;
import com.sparta.cch.backofficeproject.common.exception.ErrorCode;
import com.sparta.cch.backofficeproject.common.session.SessionConst;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 신규 관리자 회원가입 요청을 처리합니다.
     * 이메일 중복 여부와 역할 유효성을 검증한 뒤 비밀번호를 암호화하여 저장합니다.
     *
     * @param request 관리자 회원가입 요청 정보
     * @return 회원가입 결과 응답
     */
    @Transactional
    public AdminResponse signUp(AdminSignUpRequest request) {
        if (adminRepository.existsByEmail(request.getEmail())) {
            throw new ApiException(ErrorCode.DUPLICATED_EMAIL);
        }

        String roleValue = request.getRole().trim().toUpperCase();

        if ("SUPER".equals(roleValue)) {
            throw new ApiException(ErrorCode.SUPER_SIGNUP_NOT_ALLOWED);
        }
        AdminRole role;

        try {
            role = AdminRole.valueOf(roleValue);
        } catch (IllegalArgumentException exception) {
            throw new ApiException(ErrorCode.INVALID_ROLE);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Admin admin = Admin.signUp(
                request.getName(),
                request.getEmail(),
                request.getPhoneNumber(),
                encodedPassword,
                AdminStatus.PENDING,
                role
        );

        Admin savedAdmin = adminRepository.save(admin);

        return AdminResponse.createSignUpResponse(savedAdmin);
    }

}
