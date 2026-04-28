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
    public ResponseEntity<AdminApiResponse<AdminUpdateResponse>> updateAdmin(
     @PathVariable Long adminId ,
     @Valid @RequestBody AdminUpdateRequest request) {

        AdminApiResponse<AdminUpdateResponse> response = adminService.updateAdmin(adminId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 특정 관리자의 상태를 변경합니다.
     * 슈퍼 관리자만 접근할 수 있습니다.
     *
     * @param adminId 상태 변경할 관리자 ID
     * @param request 변경할 상태 값
     * @return 변경된 관리자 상태 정보
     */
    @PatchMapping("/{adminId}/status")
    public ResponseEntity<AdminApiResponse<AdminUpdateStatusResponse>> updateAdminStatus(
             @PathVariable Long adminId,
             @Valid @RequestBody AdminUpdateStatusRequest request) {

        AdminApiResponse<AdminUpdateStatusResponse> response = adminService.updateAdminStatus(adminId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 특정 관리자의 역할을 변경합니다.
     * 슈퍼 관리자만 접근할 수 있습니다.
     *
     * @param adminId 역할 변경할 관리자 ID
     * @param request 변경할 역할 값
     * @return 변경된 관리자 역할 정보
     */
    @PatchMapping("/{adminId}/role")
    public ResponseEntity<AdminApiResponse<AdminUpdateRoleResponse>> updateAdminRole(
            @PathVariable Long adminId,
            @Valid @RequestBody AdminUpdateRoleRequest request) {

        AdminApiResponse<AdminUpdateRoleResponse> response = adminService.updateAdminRole(adminId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    /**
     * 신규 관리자의 가입 신청을 승인합니다.
     * PENDING 상태의 관리자만 승인할 수 있습니다.
     * 슈퍼 관리자만 접근할 수 있습니다.
     *
     * @param adminId 승인할 관리자 ID
     * @return 승인된 관리자 정보
     */
    @PostMapping("/{adminId}/approve")
    public ResponseEntity<AdminApiResponse<AdminApproveResponse>> approveAdmin(@PathVariable Long adminId) {

        AdminApiResponse<AdminApproveResponse> response = adminService.approveAdmin(adminId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 신규 관리자의 가입 신청을 거부합니다.
     * PENDING 상태의 관리자만 거부할 수 있습니다.
     * 슈퍼 관리자만 접근할 수 있습니다.
     *
     * @param adminId 거부할 관리자 ID
     * @param request 거부 사유
     * @return 거부된 관리자 정보
     */
    @PostMapping("/{adminId}/reject")
    public ResponseEntity<AdminApiResponse<AdminRejectResponse>> rejectAdmin(
            @PathVariable Long adminId,
            @Valid @RequestBody AdminRejectRequest request) {

        AdminApiResponse<AdminRejectResponse> response = adminService.rejectAdmin(adminId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
