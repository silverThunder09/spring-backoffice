package com.sparta.cch.backofficeproject.admin.service;

import com.sparta.cch.backofficeproject.admin.dto.AdminLoginRequest;
import com.sparta.cch.backofficeproject.admin.dto.AdminResponse;
import com.sparta.cch.backofficeproject.admin.entity.Admin;
import com.sparta.cch.backofficeproject.admin.entity.AdminStatus;
import com.sparta.cch.backofficeproject.admin.repository.AdminRepository;
import com.sparta.cch.backofficeproject.common.config.PasswordEncoder;
import com.sparta.cch.backofficeproject.common.exception.ApiException;
import com.sparta.cch.backofficeproject.common.exception.ErrorCode;
import com.sparta.cch.backofficeproject.common.session.SessionConst;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 관리자 로그인 요청을 처리합니다.
     * 이메일과 비밀번호를 검증하고, 로그인 가능한 상태인지 확인한 뒤 세션에 인증 정보를 저장합니다.
     *
     * @param request 로그인 요청 정보
     * @param session 현재 HTTP 세션
     * @return 로그인 결과 응답
     */
    @Transactional(readOnly = true)
    public AdminResponse login(AdminLoginRequest request, HttpSession session) {
        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException(ErrorCode.LOGIN_FAILED));

        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new ApiException(ErrorCode.LOGIN_FAILED);
        }

        validateLoginStatus(admin);

        session.setAttribute(SessionConst.ADMIN_ID, admin.getId());
        session.setAttribute(SessionConst.ADMIN_EMAIL, admin.getEmail());
        session.setAttribute(SessionConst.ADMIN_ROLE, admin.getRole().name());

        return AdminResponse.createLoginResponse(admin);
    }

    /**
     * 관리자 로그아웃을 처리합니다.
     * 현재 세션이 존재하면 세션을 무효화합니다.
     *
     * @param session 현재 HTTP 세션
     * @return 로그아웃 결과 응답
     */
    public AdminResponse logout(HttpSession session) {
        if (session == null) {
            throw new ApiException(ErrorCode.UNAUTHORIZED);
        }

        session.invalidate();
        return AdminResponse.createLogoutResponse();
    }

    /**
     * 관리자의 상태가 로그인 가능한 상태인지 검증합니다.
     *
     * @param admin 로그인 대상 관리자
     */
    private void validateLoginStatus(Admin admin) {
        AdminStatus status = admin.getStatus();

        if (status == AdminStatus.PENDING) {
            throw new ApiException(ErrorCode.ADMIN_PENDING);
        }

        if (status == AdminStatus.REJECTED) {
            throw new ApiException(ErrorCode.ADMIN_REJECTED);
        }

        if (status == AdminStatus.SUSPENDED) {
            throw new ApiException(ErrorCode.ADMIN_SUSPENDED);
        }

        if (status == AdminStatus.INACTIVE) {
            throw new ApiException(ErrorCode.ADMIN_INACTIVE);
        }
    }

}
