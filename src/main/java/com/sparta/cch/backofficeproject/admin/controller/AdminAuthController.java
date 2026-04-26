package com.sparta.cch.backofficeproject.admin.controller;

import com.sparta.cch.backofficeproject.admin.dto.AdminLoginRequest;
import com.sparta.cch.backofficeproject.admin.dto.AdminResponse;
import com.sparta.cch.backofficeproject.admin.service.AdminAuthService;
import com.sparta.cch.backofficeproject.admin.service.AdminService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admins")
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    /**
     * 관리자의 로그인 요청을 처리하고 세션에 인증 정보를 저장합니다.
     *
     * @param request 로그인 요청 정보
     * @param session 현재 HTTP 세션
     * @return 로그인 결과 응답
     */
    @PostMapping("/login")
    public ResponseEntity<AdminResponse> login(
            @Valid @RequestBody AdminLoginRequest request,
            HttpSession session) {

        AdminResponse response = adminAuthService.login(request, session);
        return ResponseEntity.ok(response);
    }

    /**
     * 현재 로그인한 관리자의 세션을 무효화합니다.
     *
     * @param session 현재 HTTP 세션
     * @return 로그아웃 결과 응답
     */
    @PostMapping("/logout")
    public ResponseEntity<AdminResponse> logout(HttpSession session) {

        AdminResponse response = adminAuthService.logout(session);
        return ResponseEntity.ok(response);
    }

}
