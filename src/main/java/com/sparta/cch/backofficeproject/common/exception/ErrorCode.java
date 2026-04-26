package com.sparta.cch.backofficeproject.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // 공통
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "로그인 정보가 없습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN", "접근 권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND", "요청한 리소스를 찾을 수 없습니다."),
    CONFLICT(HttpStatus.CONFLICT, "CONFLICT", "요청이 현재 상태와 충돌합니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다."),
    SESSION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SESSION_ERROR", "세션 처리 중 오류가 발생했습니다."),

    // 필수값 누락
    REQUIRED_NAME(HttpStatus.BAD_REQUEST, "REQUIRED_NAME", "이름은 필수 입력값입니다."),
    REQUIRED_EMAIL(HttpStatus.BAD_REQUEST, "REQUIRED_EMAIL", "이메일은 필수 입력값입니다."),
    REQUIRED_PASSWORD(HttpStatus.BAD_REQUEST, "REQUIRED_PASSWORD", "비밀번호는 필수 입력값입니다."),
    REQUIRED_PHONE(HttpStatus.BAD_REQUEST, "REQUIRED_PHONE", "전화번호는 필수 입력값입니다."),
    REQUIRED_ROLE(HttpStatus.BAD_REQUEST, "REQUIRED_ROLE", "역할은 필수 선택값입니다."),
    REQUIRED_STATUS(HttpStatus.BAD_REQUEST, "REQUIRED_STATUS", "상태 값은 필수 입력값입니다."),
    REQUIRED_CURRENT_PASSWORD(HttpStatus.BAD_REQUEST, "REQUIRED_CURRENT_PASSWORD", "현재 비밀번호는 필수 입력값입니다."),
    REQUIRED_NEW_PASSWORD(HttpStatus.BAD_REQUEST, "REQUIRED_NEW_PASSWORD", "새 비밀번호는 필수 입력값입니다."),
    REQUIRED_NEW_PASSWORD_CONFIRM(HttpStatus.BAD_REQUEST, "REQUIRED_NEW_PASSWORD_CONFIRM", "새 비밀번호 확인은 필수 입력값입니다."),
    REQUIRED_REJECT_REASON(HttpStatus.BAD_REQUEST, "REQUIRED_REJECT_REASON", "거부 사유는 필수 입력값입니다."),
    REQUIRED_CANCEL_REASON(HttpStatus.BAD_REQUEST, "REQUIRED_CANCEL_REASON", "주문 취소 사유는 필수 입력값입니다."),

    // 형식/검증
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "INVALID_EMAIL_FORMAT", "이메일 형식이 올바르지 않습니다."),
    INVALID_PHONE_FORMAT(HttpStatus.BAD_REQUEST, "INVALID_PHONE_FORMAT", "전화번호는 010-XXXX-XXXX 형식이어야 합니다."),
    PASSWORD_TOO_SHORT(HttpStatus.BAD_REQUEST, "PASSWORD_TOO_SHORT", "비밀번호는 최소 8자 이상이어야 합니다."),
    PASSWORD_CONFIRM_MISMATCH(HttpStatus.BAD_REQUEST, "PASSWORD_CONFIRM_MISMATCH", "새 비밀번호와 비밀번호 확인이 일치하지 않습니다."),
    SAME_AS_OLD_PASSWORD(HttpStatus.BAD_REQUEST, "SAME_AS_OLD_PASSWORD", "새 비밀번호는 기존 비밀번호와 달라야 합니다."),
    INVALID_ROLE(HttpStatus.BAD_REQUEST, "INVALID_ROLE", "역할 값이 올바르지 않습니다."),
    INVALID_ADMIN_STATUS(HttpStatus.BAD_REQUEST, "INVALID_ADMIN_STATUS", "관리자 상태 값이 올바르지 않습니다."),
    INVALID_CUSTOMER_STATUS(HttpStatus.BAD_REQUEST, "INVALID_CUSTOMER_STATUS", "고객 상태 값이 올바르지 않습니다."),
    INVALID_PRODUCT_STATUS(HttpStatus.BAD_REQUEST, "INVALID_PRODUCT_STATUS", "상품 상태 값이 올바르지 않습니다."),
    INVALID_ORDER_STATUS(HttpStatus.BAD_REQUEST, "INVALID_ORDER_STATUS", "주문 상태 값이 올바르지 않습니다."),
    INVALID_CATEGORY(HttpStatus.BAD_REQUEST, "INVALID_CATEGORY", "카테고리 값이 올바르지 않습니다."),
    INVALID_PRICE(HttpStatus.BAD_REQUEST, "INVALID_PRICE", "가격은 0 이상이어야 합니다."),
    INVALID_STOCK(HttpStatus.BAD_REQUEST, "INVALID_STOCK", "재고는 0 이상이어야 합니다."),
    INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "INVALID_QUANTITY", "수량은 1 이상이어야 합니다."),
    INVALID_SORT_BY(HttpStatus.BAD_REQUEST, "INVALID_SORT_BY", "정렬 기준이 올바르지 않습니다."),
    INVALID_DIRECTION(HttpStatus.BAD_REQUEST, "INVALID_DIRECTION", "정렬 순서가 올바르지 않습니다."),
    INVALID_PAGE_NUMBER(HttpStatus.BAD_REQUEST, "INVALID_PAGE_NUMBER", "페이지 번호는 1 이상이어야 합니다."),
    INVALID_ADMIN_ID(HttpStatus.BAD_REQUEST, "INVALID_ADMIN_ID", "관리자 ID는 1 이상이어야 합니다."),
    INVALID_CUSTOMER_ID(HttpStatus.BAD_REQUEST, "INVALID_CUSTOMER_ID", "고객 ID는 1 이상이어야 합니다."),
    INVALID_PRODUCT_ID(HttpStatus.BAD_REQUEST, "INVALID_PRODUCT_ID", "상품 ID는 1 이상이어야 합니다."),
    INVALID_ORDER_ID(HttpStatus.BAD_REQUEST, "INVALID_ORDER_ID", "주문 ID는 1 이상이어야 합니다."),

    // 조회 대상 없음
    ADMIN_NOT_FOUND(HttpStatus.NOT_FOUND, "ADMIN_NOT_FOUND", "존재하지 않는 관리자입니다."),
    CUSTOMER_NOT_FOUND(HttpStatus.NOT_FOUND, "CUSTOMER_NOT_FOUND", "존재하지 않는 고객입니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT_NOT_FOUND", "존재하지 않는 상품입니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "ORDER_NOT_FOUND", "존재하지 않는 주문입니다."),

    // 인증/로그인
    SUPER_SIGNUP_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "SUPER_SIGNUP_NOT_ALLOWED", "회원가입 시 SUPER 역할은 선택할 수 없습니다."),
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "DUPLICATED_EMAIL", "이미 존재하는 이메일입니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "LOGIN_FAILED", "이메일 또는 비밀번호가 일치하지 않습니다."),
    PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "PASSWORD_MISMATCH", "현재 비밀번호가 일치하지 않습니다."),
    ADMIN_PENDING(HttpStatus.FORBIDDEN, "ADMIN_PENDING", "슈퍼 관리자의 승인을 기다리고 있습니다. 승인 후 로그인 부탁드립니다."),
    ADMIN_REJECTED(HttpStatus.FORBIDDEN, "ADMIN_REJECTED", "관리자 계정 신청이 거부되었습니다."),
    ADMIN_SUSPENDED(HttpStatus.FORBIDDEN, "ADMIN_SUSPENDED", "정지된 관리자 계정입니다. 관리자에게 문의해주세요."),
    ADMIN_INACTIVE(HttpStatus.FORBIDDEN, "ADMIN_INACTIVE", "비활성화된 관리자 계정입니다. 관리자에게 문의해주세요."),

    // 관리자 관리
    ALREADY_APPROVED_ADMIN(HttpStatus.CONFLICT, "ALREADY_APPROVED_ADMIN", "이미 승인 처리된 관리자입니다."),
    ALREADY_REJECTED_ADMIN(HttpStatus.CONFLICT, "ALREADY_REJECTED_ADMIN", "이미 거부 처리된 관리자입니다."),
    SELF_DELETE_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "SELF_DELETE_NOT_ALLOWED", "본인 계정은 삭제할 수 없습니다."),

    // 고객/상품/주문 비즈니스
    PRODUCT_DISCONTINUED(HttpStatus.BAD_REQUEST, "PRODUCT_DISCONTINUED", "단종 상품은 주문할 수 없습니다."),
    PRODUCT_SOLD_OUT(HttpStatus.BAD_REQUEST, "PRODUCT_SOLD_OUT", "품절 상품은 주문할 수 없습니다."),
    INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "INSUFFICIENT_STOCK", "재고가 부족합니다."),
    ORDER_STATUS_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "ORDER_STATUS_NOT_ALLOWED", "현재 주문 상태에서는 해당 작업을 수행할 수 없습니다."),
    ORDER_CANCEL_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "ORDER_CANCEL_NOT_ALLOWED", "준비중 상태의 주문만 취소할 수 있습니다."),
    INVALID_ORDER_TRANSITION(HttpStatus.BAD_REQUEST, "INVALID_ORDER_TRANSITION", "현재 주문 상태에서는 해당 상태로 변경할 수 없습니다."),
    ALREADY_CANCELED_ORDER(HttpStatus.BAD_REQUEST, "ALREADY_CANCELED_ORDER", "이미 취소된 주문입니다."),


    COMPLETED_ORDER_STATUS_CHANGE_NOT_ALLOWED(
            HttpStatus.BAD_REQUEST,
            "COMPLETED_ORDER_STATUS_CHANGE_NOT_ALLOWED",
            "이미 완료된 주문은 상태를 변경할 수 없습니다."
    ),
    CANCELED_ORDER_STATUS_CHANGE_NOT_ALLOWED(
            HttpStatus.BAD_REQUEST,
            "CANCELED_ORDER_STATUS_CHANGE_NOT_ALLOWED",
            "취소된 주문은 상태를 변경할 수 없습니다."
    ),
    PRODUCT_DELETE_CONFLICT(
            HttpStatus.CONFLICT,
            "PRODUCT_DELETE_CONFLICT",
            "해당 상품과 연관된 활성 주문이 존재하여 삭제할 수 없습니다."
    );

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
