package com.sparta.cch.backofficeproject.common.interceptor;

import com.sparta.cch.backofficeproject.admin.entity.AdminRole;
import com.sparta.cch.backofficeproject.common.exception.ApiException;
import com.sparta.cch.backofficeproject.common.exception.ErrorCode;
import com.sparta.cch.backofficeproject.common.session.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SuperAdminCheckInterceptor implements HandlerInterceptor {

    /**
     * 요청을 처리하기 전에 현재 로그인한 관리자가 슈퍼 관리자인지 확인합니다.
     *
     * @param request 현재 HTTP 요청
     * @param response 현재 HTTP 응답
     * @param handler 실행 대상 핸들러
     * @return 요청 계속 진행 여부
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(SessionConst.ADMIN_ROLE) == null) {
            throw new ApiException(ErrorCode.UNAUTHORIZED);
        }

        String adminRole = String.valueOf(session.getAttribute(SessionConst.ADMIN_ROLE));

        if (!AdminRole.SUPER.name().equals(adminRole)) {
            throw new ApiException(ErrorCode.FORBIDDEN, "슈퍼 관리자만 접근할 수 있습니다.");
        }

        return true;
    }
}
