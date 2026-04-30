package com.sparta.cch.backofficeproject.admin.controller;

import com.sparta.cch.backofficeproject.admin.dto.*;
import com.sparta.cch.backofficeproject.admin.service.AdminService;
import com.sparta.cch.backofficeproject.common.dto.CommonResponse;
import com.sparta.cch.backofficeproject.common.session.SessionConst;
import jakarta.servlet.http.HttpSession;
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
    public ResponseEntity<CommonResponse<AdminSignUpResponse>> signUp(@Valid @RequestBody AdminSignUpRequest request) {

        AdminSignUpResponse response = adminService.signUp(request);

        CommonResponse<AdminSignUpResponse> data = CommonResponse.success(
                HttpStatus.CREATED.value(),
                "관리자 회원가입 신청이 완료되었습니다. 슈퍼 관리자의 승인을 기다려주세요.",
                response
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(data);
    }

    /**
     * 관리자 목록을 조회합니다.
     * 슈퍼 관리자만 접근할 수 있습니다.
     *
     * @param request 검색 키워드, 페이징, 정렬, 역할/상태 필터 조건
     * @return 관리자 목록 및 페이징 정보
     */
    @GetMapping
    public ResponseEntity<CommonResponse<AdminListResponse>> getAll(@Valid AdminListRequest request) {

        AdminListResponse response = adminService.getAll(request);

        CommonResponse<AdminListResponse> data = CommonResponse.success(
                HttpStatus.OK.value(),
                "관리자 목록 조회에 성공했습니다.",
                response
        );

        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    /**
     * 특정 관리자의 상세 정보를 조회합니다.
     * 슈퍼 관리자만 접근할 수 있습니다.
     *
     * @param adminId 조회할 관리자 ID
     * @return 관리자 상세 정보
     */
    @GetMapping("/{adminId}")
    public ResponseEntity<CommonResponse<AdminDetailResponse>> getAdmin(@PathVariable Long adminId) {

        AdminDetailResponse response = adminService.getAdmin(adminId);

        CommonResponse<AdminDetailResponse> data = CommonResponse.success(
                HttpStatus.OK.value(),
                "관리자 상세 조회에 성공했습니다.",
                response
        );

        return ResponseEntity.status(HttpStatus.OK).body(data);
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
    public ResponseEntity<CommonResponse<AdminUpdateResponse>> updateAdmin(
            @PathVariable Long adminId,
            @Valid @RequestBody AdminUpdateRequest request) {

        AdminUpdateResponse response = adminService.updateAdmin(adminId, request);

        CommonResponse<AdminUpdateResponse> data = CommonResponse.success(
                HttpStatus.OK.value(),
                "관리자 정보 수정에 성공했습니다.",
                response
        );

        return ResponseEntity.status(HttpStatus.OK).body(data);
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
    public ResponseEntity<CommonResponse<AdminUpdateStatusResponse>> updateAdminStatus(
            @PathVariable Long adminId,
            @Valid @RequestBody AdminUpdateStatusRequest request) {

        AdminUpdateStatusResponse response = adminService.updateAdminStatus(adminId, request);

        CommonResponse<AdminUpdateStatusResponse> data = CommonResponse.success(
                HttpStatus.OK.value(),
                "관리자 상태 변경에 성공했습니다.",
                response
        );

        return ResponseEntity.status(HttpStatus.OK).body(data);
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
    public ResponseEntity<CommonResponse<AdminUpdateRoleResponse>> updateAdminRole(
            @PathVariable Long adminId,
            @Valid @RequestBody AdminUpdateRoleRequest request) {

        AdminUpdateRoleResponse response = adminService.updateAdminRole(adminId, request);

        CommonResponse<AdminUpdateRoleResponse> data = CommonResponse.success(
                HttpStatus.OK.value(),
                "관리자 역할 변경에 성공했습니다.",
                response
        );

        return ResponseEntity.status(HttpStatus.OK).body(data);
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
    public ResponseEntity<CommonResponse<AdminApproveResponse>> approveAdmin(@PathVariable Long adminId) {

        AdminApproveResponse response = adminService.approveAdmin(adminId);

        CommonResponse<AdminApproveResponse> data = CommonResponse.success(
                HttpStatus.OK.value(),
                "관리자 승인 처리에 성공했습니다.",
                response
        );

        return ResponseEntity.status(HttpStatus.OK).body(data);
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
    public ResponseEntity<CommonResponse<AdminRejectResponse>> rejectAdmin(
            @PathVariable Long adminId,
            @Valid @RequestBody AdminRejectRequest request) {

        AdminRejectResponse response = adminService.rejectAdmin(adminId, request);

        CommonResponse<AdminRejectResponse> data = CommonResponse.success(
                HttpStatus.OK.value(),
                "관리자 거부 처리에 성공했습니다.",
                response
        );

        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    /**
     * 특정 관리자를 삭제합니다.
     * 슈퍼 관리자만 접근할 수 있습니다.
     *
     * @param adminId        삭제할 관리자 ID
     * @param sessionAdminId 현재 로그인한 슈퍼 관리자 ID
     * @return 삭제된 관리자 ID
     */
    @DeleteMapping("/{adminId}")
    public ResponseEntity<CommonResponse<AdminDeleteResponse>> deleteAdmin(
            @PathVariable Long adminId,
            @SessionAttribute(SessionConst.ADMIN_ID) Long sessionAdminId) {

        AdminDeleteResponse response = adminService.deleteAdmin(adminId, sessionAdminId);

        CommonResponse<AdminDeleteResponse> data = CommonResponse.success(
                HttpStatus.OK.value(),
                "관리자 삭제에 성공했습니다.",
                response
        );

        return ResponseEntity.ok(data);
    }

    /**
     * 로그인한 관리자의 비밀번호를 변경합니다.
     * 비밀번호 변경 후 세션이 무효화됩니다.
     *
     * @param sessionAdminId 현재 로그인한 관리자 ID
     * @param request        현재 비밀번호, 새 비밀번호, 새 비밀번호 확인
     * @param session        현재 HTTP 세션
     * @return 비밀번호 변경 결과 응답
     */
    @PatchMapping("/password")
    public ResponseEntity<CommonResponse<Void>> changePassword(
            @SessionAttribute(name = SessionConst.ADMIN_ID) Long sessionAdminId,
            @Valid @RequestBody AdminChangePasswordRequest request,
            HttpSession session) {

        adminService.changePassword(sessionAdminId, request, session);

        CommonResponse<Void> data = CommonResponse.success(
                HttpStatus.OK.value(),
                "비밀번호 변경에 성공했습니다. 다시 로그인해주세요.",
                null
        );

        return ResponseEntity.ok(data);
    }

    /**
     * 로그인한 관리자의 내 프로필 정보를 조회합니다.
     *
     * @param sessionAdminId 현재 로그인한 관리자 ID
     * @return 내 프로필 조회 결과 응답
     */
    @GetMapping("/me")
    public ResponseEntity<CommonResponse<AdminMyProfileResponse>> getMyProfile(
            @SessionAttribute(name = SessionConst.ADMIN_ID) Long sessionAdminId) {

        AdminMyProfileResponse response = adminService.getMyProfile(sessionAdminId);

        CommonResponse<AdminMyProfileResponse> data = CommonResponse.success(
                HttpStatus.OK.value(),
                "내 프로필 조회에 성공했습니다.",
                response
        );

        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    /**
     * 로그인한 관리자의 내 프로필 정보를 수정합니다.
     *
     * @param sessionAdminId 현재 로그인한 관리자 ID
     * @param request        수정할 이름, 이메일, 전화번호
     * @return 내 프로필 수정 결과 응답
     */
    @PutMapping("/me")
    public ResponseEntity<CommonResponse<AdminMyProfileUpdateResponse>> updateMyProfile(
            @SessionAttribute(name = SessionConst.ADMIN_ID) Long sessionAdminId,
            @Valid @RequestBody AdminMyProfileUpdateRequest request
    ) {
        AdminMyProfileUpdateResponse response = adminService.updateMyProfile(sessionAdminId, request);

        CommonResponse<AdminMyProfileUpdateResponse> data = CommonResponse.success(
                HttpStatus.OK.value(),
                "내 프로필 수정에 성공했습니다.",
                response
        );

        return ResponseEntity.status(HttpStatus.OK).body(data);
    }
}

