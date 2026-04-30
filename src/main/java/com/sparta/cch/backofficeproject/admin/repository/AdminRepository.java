package com.sparta.cch.backofficeproject.admin.repository;

import com.sparta.cch.backofficeproject.admin.entity.Admin;
import com.sparta.cch.backofficeproject.admin.entity.AdminRole;
import com.sparta.cch.backofficeproject.admin.entity.AdminStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {


    boolean existsByRole(AdminRole role);

    Optional<Admin> findByEmail(String email);


    /**
     * 검색 조건에 맞는 관리자 목록을 페이징하여 조회합니다.
     *
     * <p>각 조건이 null이면 해당 조건은 무시되며,
     * 모든 조건이 null이면 전체 목록을 반환합니다.</p>
     *
     * @param keyword 이름/이메일 검색 키워드 (null이면 전체 검색, 대소문자 구분 없음)
     * @param role    역할 필터 (null이면 전체 역할 조회)
     * @param status  상태 필터 (null이면 전체 상태 조회)
     * @param pageable 페이징 및 정렬 정보
     * @return 조건에 맞는 관리자 목록 (페이징 적용)
     */
    @Query("""
            SELECT a 
            FROM Admin a
            WHERE (:keyword IS NULL
                   OR lower(a.name) LIKE lower(concat('%', :keyword, '%'))
                   OR lower(a.email) LIKE lower(concat('%', :keyword, '%')))
              AND (:role IS NULL OR a.role = :role)
              AND (:status IS NULL OR a.status = :status)
            """)
    Page<Admin> searchAdmins(@Param("keyword") String keyword,
                             @Param("role") AdminRole role,
                             @Param("status") AdminStatus status,
                             Pageable pageable);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM admins WHERE email = :email)", nativeQuery = true)
    int existsByEmailIncludeDeleted(@Param("email") String email);
}
