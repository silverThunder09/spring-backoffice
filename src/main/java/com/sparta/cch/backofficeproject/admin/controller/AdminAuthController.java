package com.sparta.cch.backofficeproject.admin.controller;

import com.sparta.cch.backofficeproject.admin.dto.AdminLoginRequest;
import com.sparta.cch.backofficeproject.admin.dto.AdminLoginResponse;
import com.sparta.cch.backofficeproject.admin.service.AdminAuthService;
import com.sparta.cch.backofficeproject.common.dto.CommonResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<CommonResponse<AdminLoginResponse>> login(
            @Valid @RequestBody AdminLoginRequest request,
            HttpSession session) {

        AdminLoginResponse response = adminAuthService.login(request, session);

        CommonResponse<AdminLoginResponse> data = CommonResponse.success(
                HttpStatus.OK.value(),
                "로그인에 성공했습니다.",
                response
        );

        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    /**
     * 현재 로그인한 관리자의 세션을 무효화합니다.
     *
     * @param session 현재 HTTP 세션
     * @return 로그아웃 결과 응답
     */
    @PostMapping("/logout")
    public ResponseEntity<CommonResponse<Void>> logout(HttpSession session) {

        adminAuthService.logout(session);

        CommonResponse<Void> response = CommonResponse.success(
                HttpStatus.OK.value(),
                "로그아웃에 성공했습니다.",
                null
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
