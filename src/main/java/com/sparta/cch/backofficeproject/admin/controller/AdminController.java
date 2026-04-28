package com.sparta.cch.backofficeproject.admin.controller;

import com.sparta.cch.backofficeproject.admin.dto.*;
import com.sparta.cch.backofficeproject.admin.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<AdminApiResponse<AdminSignUpResponse>> signUp(@Valid @RequestBody AdminSignUpRequest request) {
        AdminApiResponse<AdminSignUpResponse> response = adminService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 관리자 목록을 조회합니다.
     * 슈퍼 관리자만 접근할 수 있습니다.
     *
     * @param request 검색 키워드, 페이징, 정렬, 역할/상태 필터 조건
     * @return 관리자 목록 및 페이징 정보
     */
    @GetMapping
    public ResponseEntity<AdminApiResponse<AdminListResponse>> getAll(@Valid AdminListRequest request) {

        AdminApiResponse<AdminListResponse> response = adminService.getAll(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 특정 관리자의 상세 정보를 조회합니다.
     * 슈퍼 관리자만 접근할 수 있습니다.
     *
     * @param adminId 조회할 관리자 ID
     * @return 관리자 상세 정보
     */
    @GetMapping("/{adminId}")
    public ResponseEntity<AdminApiResponse<AdminDetailResponse>> getAdmin(@PathVariable Long adminId) {

        AdminApiResponse<AdminDetailResponse> response = adminService.getAdmin(adminId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 특정 관리자의 정보를 수정합니다.
     * 슈퍼 관리자만 접근할 수 있습니다.
     *
     * @param adminId 수정할 관리자 ID
     * @param request 수정할 이름, 이메일, 전화번호
     * @return 수정된 관리자 정보
     */
    @PutMapping("/{adminId}")
    public ResponseEntity<AdminApiResponse<AdminUpdateResponse>> updateAdmin(@PathVariable Long adminId ,
                                                                             @Valid @RequestBody AdminUpdateRequest request) {

        AdminApiResponse<AdminUpdateResponse> response = adminService.updateAdmin(adminId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
