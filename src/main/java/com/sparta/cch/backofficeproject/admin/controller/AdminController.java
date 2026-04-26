package com.sparta.cch.backofficeproject.admin.controller;

import com.sparta.cch.backofficeproject.admin.dto.AdminResponse;
import com.sparta.cch.backofficeproject.admin.dto.AdminSignUpRequest;
import com.sparta.cch.backofficeproject.admin.service.AdminService;
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
public class AdminController {

    private final AdminService adminService;

    /**
     * 신규 관리자의 회원가입 신청을 처리합니다.
     * 회원가입이 완료되면 관리자는 승인 대기 상태로 저장됩니다.
     *
     * @param request 관리자 회원가입 요청 정보
     * @return 회원가입 결과 응답
     */
    @PostMapping("/signup")
    public ResponseEntity<AdminResponse> signUp(@Valid @RequestBody AdminSignUpRequest request) {

        AdminResponse response = adminService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
