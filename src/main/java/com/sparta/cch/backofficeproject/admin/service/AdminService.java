package com.sparta.cch.backofficeproject.admin.service;

import com.sparta.cch.backofficeproject.admin.dto.*;
import com.sparta.cch.backofficeproject.admin.entity.Admin;
import com.sparta.cch.backofficeproject.admin.entity.AdminRole;
import com.sparta.cch.backofficeproject.admin.entity.AdminStatus;
import com.sparta.cch.backofficeproject.admin.repository.AdminRepository;
import com.sparta.cch.backofficeproject.common.config.PasswordEncoder;
import com.sparta.cch.backofficeproject.common.exception.ApiException;
import com.sparta.cch.backofficeproject.common.exception.ErrorCode;
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
    public AdminApiResponse<AdminSignUpResponse> signUp(AdminSignUpRequest request) {
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
                request.getPhone(),
                encodedPassword,
                AdminStatus.PENDING,
                role
        );

        Admin savedAdmin = adminRepository.save(admin);

        AdminSignUpResponse data = AdminSignUpResponse.of(savedAdmin);

        return AdminApiResponse.success(
                201,
                "관리자 회원가입 신청이 완료되었습니다. 슈퍼 관리자의 승인을 기다려주세요.",
                data
        );
    }


    /**
     * 관리자 목록을 조회합니다.
     *
     * <p>keyword가 존재하면 이름/이메일 부분 일치 검색을 수행하고,
     * role, status가 존재하면 해당 조건으로 필터링합니다.
     * 모든 조건이 없으면 전체 목록을 반환합니다.</p>
     *
     * @param request 검색 키워드, 페이징, 정렬, 역할/상태 필터 조건
     * @return 관리자 목록 및 페이징 정보
     */
    @Transactional(readOnly = true)
    public AdminApiResponse<AdminListResponse> getAll(AdminListRequest request) {

        // 페이지 번호는 0-based이므로 1감소, 정렬 기준/순서 적용
        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getSize(),
                Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy())
        );

        // 빈 문자열이면 null로 변환
        String keyword = StringUtils.hasText(request.getKeyword())
                ? request.getKeyword().trim() : null;

        // keyword, role, status 조건에 맞는 관리자 목록 조회
        Page<Admin> adminPage = adminRepository.searchAdmins(
                keyword, request.getRole(), request.getStatus(), pageable
        );

        AdminListResponse data = AdminListResponse.of(adminPage);

        return AdminApiResponse.success(
                200,
                "관리자 목록 조회에 성공했습니다.",
                data
        );
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
    public AdminApiResponse<AdminDetailResponse> getAdmin(Long adminId) {

        Admin admin = findAdminById(adminId);

        AdminDetailResponse data = AdminDetailResponse.of(admin);

        return AdminApiResponse.success(
                200,
                "관리자 상세 조회에 성공했습니다.",
                data
        );
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
    public AdminApiResponse<AdminUpdateResponse> updateAdmin(Long adminId, AdminUpdateRequest request) {

        Admin admin = findAdminById(adminId);

        // 본인 이메일 제외하고 중복 체크
        if (!admin.getEmail().equals(request.getEmail())
                && adminRepository.existsByEmail(request.getEmail())) {
            throw new ApiException(ErrorCode.DUPLICATED_EMAIL);
        }

        admin.update(request.getName(), request.getEmail(), request.getPhone());

        AdminUpdateResponse data = AdminUpdateResponse.of(admin);

        return AdminApiResponse.success(
                200,
                "관리자 정보 수정에 성공했습니다.",
                data
        );
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
    public AdminApiResponse<AdminUpdateStatusResponse> updateAdminStatus(
            Long adminId,
            AdminUpdateStatusRequest request) {

        Admin admin = findAdminById(adminId);

        AdminStatus status;

        try {
            status = AdminStatus.valueOf(request.getStatus().trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApiException(ErrorCode.INVALID_ADMIN_STATUS);
        }

        admin.updateStatus(status);
        AdminUpdateStatusResponse data = AdminUpdateStatusResponse.of(admin);

        return AdminApiResponse.success(
                200,
                "관리자 상태 변경에 성공했습니다.",
                data
        );
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
    public AdminApiResponse<AdminUpdateRoleResponse> updateAdminRole(
            Long adminId,
            AdminUpdateRoleRequest request) {
        Admin admin = findAdminById(adminId);

        AdminRole role;

        try {
            role = AdminRole.valueOf(request.getRole().trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApiException(ErrorCode.INVALID_ROLE);
        }

        admin.updateRole(role);
        AdminUpdateRoleResponse data = AdminUpdateRoleResponse.of(admin);

        return AdminApiResponse.success(
                200,
                "관리자 역할 변경에 성공했습니다.",
                data
        );
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


}
