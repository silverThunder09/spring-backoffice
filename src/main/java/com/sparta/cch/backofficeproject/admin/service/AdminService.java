package com.sparta.cch.backofficeproject.admin.service;

import com.sparta.cch.backofficeproject.admin.dto.*;
import com.sparta.cch.backofficeproject.admin.entity.Admin;
import com.sparta.cch.backofficeproject.admin.entity.AdminRole;
import com.sparta.cch.backofficeproject.admin.entity.AdminStatus;
import com.sparta.cch.backofficeproject.admin.repository.AdminRepository;
import com.sparta.cch.backofficeproject.common.config.PasswordEncoder;
import com.sparta.cch.backofficeproject.common.exception.ApiException;
import com.sparta.cch.backofficeproject.common.exception.ErrorCode;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
    public AdminSignUpResponse signUp(AdminSignUpRequest request) {

        validateEmailNotDuplicated(request.getEmail());

        //  requestDto 안쪽으로 넣어라
        AdminRole role = parseSignUpRole(request.getRole());

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Admin savedAdmin = adminRepository.save(Admin.signUp(request, role, encodedPassword));

        return AdminSignUpResponse.of(savedAdmin);
    }

    // 이메일 중복 공통 메서드
    private void validateEmailNotDuplicated(String email) {
        if (adminRepository.existsByEmailIncludeDeleted(email) == 1) {
            throw new ApiException(ErrorCode.DUPLICATED_EMAIL);
        }
    }

    // 회원가입 요청에 포함된 역할 문자열을 관리자 역할 enum 변환
    // 컨트롤러에서 나타내는게 맞음
    private AdminRole parseSignUpRole(String roleValue) {
        AdminRole role;

        try {
            role = AdminRole.from(roleValue);
        } catch (IllegalArgumentException exception) {
            throw new ApiException(ErrorCode.INVALID_ROLE);
        }

        if (role == AdminRole.SUPER) {
            throw new ApiException(ErrorCode.SUPER_SIGNUP_NOT_ALLOWED);
        }
        return role;
    }


    /**
     * 관리자 목록을 조회합니다.
     * <p>
     * keyword가 존재하면 이름/이메일 부분 일치 검색을 수행하고,
     * role, status가 존재하면 해당 조건으로 필터링합니다.
     * 모든 조건이 없으면 전체 목록을 반환합니다.
     * </p>
     *
     * @param request 검색 키워드, 페이징, 정렬, 역할/상태 필터 조건
     * @return 관리자 목록 및 페이징 정보
     */
    @Transactional(readOnly = true)
    public AdminListResponse getAll(AdminListRequest request) {

        // 페이지 번호는 0-based이므로 1감소, 정렬 기준/순서 적용
        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getSize(),
                Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy())
        );

        // 빈 문자열이면 null로 변환
        String keyword = StringUtils.hasText(request.getKeyword())
                ? request.getKeyword().trim()
                : null;

        // keyword, role, status 조건에 맞는 관리자 목록 조회
        Page<Admin> adminPage = adminRepository.searchAdmins(
                keyword,
                request.getRole(),
                request.getStatus(),
                pageable
        );

        return AdminListResponse.of(adminPage);
    }

    /**
     * 특정 관리자의 상세 정보를 조회합니다.
     * 슈퍼 관리자만 접근할 수 있습니다.
     *
     * @param adminId 조회할 관리자 ID
     * @return 관리자 상세 정보
     * @throws ApiException 관리자가 존재하지 않을 경우 (ADMIN_NOT_FOUND)
     */
    @Transactional(readOnly = true)
    public AdminDetailResponse getAdmin(Long adminId) {
        Admin admin = findAdminById(adminId);
        return AdminDetailResponse.of(admin);
    }

    /**
     * 특정 관리자의 정보를 수정합니다.
     * 슈퍼 관리자만 접근할 수 있습니다.
     *
     * @param adminId 수정할 관리자 ID
     * @param request 수정할 이름, 이메일, 전화번호
     * @return 수정된 관리자 정보
     * @throws ApiException 관리자 ID가 1 미만인 경우 (INVALID_ADMIN_ID)
     * @throws ApiException 관리자가 존재하지 않는 경우 (ADMIN_NOT_FOUND)
     * @throws ApiException 이메일이 중복된 경우 (DUPLICATED_EMAIL)
     */
    @Transactional
    public AdminUpdateResponse updateAdmin(Long adminId, AdminUpdateRequest request) {

        Admin admin = findAdminById(adminId);

        // 본인 이메일 제외하고 중복 체크
        if (!admin.getEmail().equals(request.getEmail())
                && adminRepository.existsByEmailIncludeDeleted(request.getEmail()) == 1) {
            throw new ApiException(ErrorCode.DUPLICATED_EMAIL);
        }

        admin.update(request.getName(), request.getEmail(), request.getPhone());

        return AdminUpdateResponse.of(admin);
    }

    /**
     * 특정 관리자의 상태를 변경합니다.
     * 슈퍼 관리자만 접근할 수 있습니다.
     *
     * @param adminId 상태 변경할 관리자 ID
     * @param request 변경할 상태 값
     * @return 변경된 관리자 상태 정보
     * @throws ApiException 관리자가 존재하지 않는 경우 (ADMIN_NOT_FOUND)
     * @throws ApiException 상태 값이 올바르지 않은 경우 (INVALID_REQUEST)
     */
    @Transactional
    public AdminUpdateStatusResponse updateAdminStatus(Long adminId, AdminUpdateStatusRequest request) {

        Admin admin = findAdminById(adminId);

        AdminStatus status;

        try {
            status = AdminStatus.valueOf(request.getStatus().trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApiException(ErrorCode.INVALID_ADMIN_STATUS);
        }

        admin.updateStatus(status);

        return AdminUpdateStatusResponse.of(admin);
    }

    /**
     * 특정 관리자의 역할을 변경합니다.
     * 슈퍼 관리자만 접근할 수 있습니다.
     *
     * @param adminId 역할 변경할 관리자 ID
     * @param request 변경할 역할 값
     * @return 변경된 관리자 역할 정보
     * @throws ApiException 관리자가 존재하지 않는 경우 (ADMIN_NOT_FOUND)
     * @throws ApiException 역할 값이 올바르지 않은 경우 (INVALID_ROLE)
     */
    @Transactional
    public AdminUpdateRoleResponse updateAdminRole(Long adminId, AdminUpdateRoleRequest request) {
        Admin admin = findAdminById(adminId);

        AdminRole role;

        try {
            role = AdminRole.valueOf(request.getRole().trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApiException(ErrorCode.INVALID_ROLE);
        }

        admin.updateRole(role);

        return AdminUpdateRoleResponse.of(admin);
    }

    /**
     * 관리자의 가입 신청을 승인합니다.
     * PENDING 상태의 관리자만 승인할 수 있습니다.
     * 슈퍼 관리자만 접근할 수 있습니다.
     *
     * @param adminId 승인할 관리자 ID
     * @return 승인된 관리자 정보
     * @throws ApiException 관리자가 존재하지 않을 경우 (ADMIN_NOT_FOUND)
     * @throws ApiException 이미 승인된 관리자인 경우 (ALREADY_APPROVED_ADMIN)
     * @throws ApiException 이미 거부된 관리자인 경우 (ALREADY_REJECTED_ADMIN)
     * @throws ApiException PENDING 상태가 아닌 경우 (INVALID_REQUEST)
     */
    @Transactional
    public AdminApproveResponse approveAdmin(Long adminId) {

        Admin admin = findAdminById(adminId);

        // PENDING 상태가 아니면 예외 처리
        validatePendingStatus(admin);

        admin.approve();

        return AdminApproveResponse.of(admin);
    }

    /**
     * 신규 관리자의 가입 신청을 거부합니다.
     * PENDING 상태의 관리자만 거부할 수 있습니다.
     * 슈퍼 관리자만 접근할 수 있습니다.
     *
     * @param adminId 거부할 관리자 ID
     * @param request 거부 사유
     * @return 거부된 관리자 정보
     * @throws ApiException 관리자가 존재하지 않는 경우 (ADMIN_NOT_FOUND)
     * @throws ApiException 이미 승인된 관리자인 경우 (ALREADY_APPROVED_ADMIN)
     * @throws ApiException 이미 거부된 관리자인 경우 (ALREADY_REJECTED_ADMIN)
     * @throws ApiException PENDING 상태가 아닌 경우 (INVALID_REQUEST)
     */
    @Transactional
    public AdminRejectResponse rejectAdmin(Long adminId, AdminRejectRequest request) {

        Admin admin = findAdminById(adminId);

        validatePendingStatus(admin);

        admin.reject(request.getRejectReason());

        return AdminRejectResponse.of(admin);
    }

    /**
     * 특정 관리자를 삭제합니다. (Soft Delete)
     * 슈퍼 관리자만 접근할 수 있습니다.
     * 본인 계정은 삭제할 수 없습니다.
     *
     * @param adminId        삭제할 관리자 ID
     * @param sessionAdminId 현재 로그인한 슈퍼 관리자 ID
     * @return 삭제된 관리자 ID
     * @throws ApiException 관리자가 존재하지 않는 경우 (ADMIN_NOT_FOUND)
     * @throws ApiException 본인 계정을 삭제하려는 경우 (INVALID_REQUEST)
     */
    @Transactional
    public AdminDeleteResponse deleteAdmin(Long adminId, Long sessionAdminId) {

        if (adminId.equals(sessionAdminId)) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "본인 계정은 삭제할 수 없습니다.");
        }

        Admin admin = findAdminById(adminId);
        adminRepository.delete(admin);

        return AdminDeleteResponse.of(adminId);
    }

    /**
     * 로그인한 관리자의 비밀번호를 변경합니다.
     * 비밀번호 변경 후 세션을 무효화합니다.
     *
     * @param sessionAdminId 현재 로그인한 관리자 ID
     * @param request        현재 비밀번호, 새 비밀번호, 새 비밀번호 확인
     * @param session        현재 HTTP 세션
     * @throws ApiException 새 비밀번호와 확인이 일치하지 않는 경우 (INVALID_REQUEST)
     * @throws ApiException 현재 비밀번호가 일치하지 않는 경우 (PASSWORD_MISMATCH)
     * @throws ApiException 관리자가 존재하지 않는 경우 (ADMIN_NOT_FOUND)
     */
    @Transactional
    public void changePassword(Long sessionAdminId, AdminChangePasswordRequest request, HttpSession session) {

        // 새 비밀번호 확인 일치 여부 검증
        if (!request.getNewPassword().equals(request.getNewPasswordConfirm())) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        Admin admin = findAdminById(sessionAdminId);

        // 현재 비밀번호 일치 여부 검증
        if (!passwordEncoder.matches(request.getCurrentPassword(), admin.getPassword())) {
            throw new ApiException(ErrorCode.PASSWORD_MISMATCH);
        }

        admin.changePassword(passwordEncoder.encode(request.getNewPassword()));
        session.invalidate();
    }

    /**
     * 관리자 ID로 관리자를 조회합니다.
     *
     * @param adminId 조회할 관리자 ID
     * @return 관리자 엔티티
     * @throws ApiException ID가 1 미만인 경우 (INVALID_ADMIN_ID)
     * @throws ApiException 관리자가 존재하지 않는 경우 (ADMIN_NOT_FOUND)
     */
    private Admin findAdminById(Long adminId) {
        if (adminId < 1) {
            throw new ApiException(ErrorCode.INVALID_ADMIN_ID);
        }
        return adminRepository.findById(adminId)
                .orElseThrow(() -> new ApiException(ErrorCode.ADMIN_NOT_FOUND));
    }

    /**
     * 관리자 상태가 PENDING인지 검증합니다.
     *
     * @param admin 검증할 관리자 엔티티
     * @throws ApiException 이미 승인된 관리자인 경우 (ALREADY_APPROVED_ADMIN)
     * @throws ApiException 이미 거부된 관리자인 경우 (ALREADY_REJECTED_ADMIN)
     * @throws ApiException PENDING 상태가 아닌 경우 (INVALID_REQUEST)
     */
    private void validatePendingStatus(Admin admin) {
        if (admin.getStatus() == AdminStatus.ACTIVE) {
            throw new ApiException(ErrorCode.ALREADY_APPROVED_ADMIN);
        }
        if (admin.getStatus() == AdminStatus.REJECTED) {
            throw new ApiException(ErrorCode.ALREADY_REJECTED_ADMIN);
        }
        if (admin.getStatus() != AdminStatus.PENDING) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "승인대기 상태의 관리자만 처리할 수 있습니다.");
        }
    }

    /**
     * 로그인한 관리자의 내 프로필 정보를 조회합니다.
     * <p>
     * 세션에 저장된 관리자 ID로 현재 로그인한 관리자를 조회하고,
     * 조회된 엔티티를 내 프로필 응답 DTO로 변환하여 반환합니다.
     * </p>
     *
     * @param sessionAdminId 현재 로그인한 관리자 ID
     * @return 내 프로필 조회 결과 응답
     */
    @Transactional(readOnly = true)
    public AdminMyProfileResponse getMyProfile(Long sessionAdminId) {

        Admin admin = findAdminById(sessionAdminId);

        return AdminMyProfileResponse.of(admin);
    }

    /**
     * 로그인한 관리자의 내 프로필 정보를 수정합니다.
     * <p>
     * 세션에 저장된 관리자 ID로 현재 로그인한 관리자를 조회하고,
     * 이메일 중복 여부를 확인한 뒤 이름, 이메일, 전화번호를 수정합니다.
     * </p>
     *
     * @param sessionAdminId 현재 로그인한 관리자 ID
     * @param request        수정할 이름, 이메일, 전화번호
     * @return 내 프로필 수정 결과 응답
     */
    @Transactional
    public AdminMyProfileUpdateResponse updateMyProfile(Long sessionAdminId, AdminMyProfileUpdateRequest request) {
        Admin admin = findAdminById(sessionAdminId);

        // 본인 이메일 제외하고 중복 체크
        if (!admin.getEmail().equals(request.getEmail())
                && adminRepository.existsByEmailIncludeDeleted(request.getEmail()) == 1) {
            throw new ApiException(ErrorCode.DUPLICATED_EMAIL);
        }

        admin.update(request.getName(), request.getEmail(), request.getPhone());

        return AdminMyProfileUpdateResponse.of(admin);
    }
}