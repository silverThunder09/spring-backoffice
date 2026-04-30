package com.sparta.cch.backofficeproject.admin.controller;


import com.sparta.cch.backofficeproject.admin.dto.AdminSignUpRequest;
import com.sparta.cch.backofficeproject.admin.entity.Admin;
import com.sparta.cch.backofficeproject.admin.entity.AdminRole;
import com.sparta.cch.backofficeproject.admin.repository.AdminRepository;
import com.sparta.cch.backofficeproject.common.config.PasswordEncoder;
import com.sparta.cch.backofficeproject.common.session.SessionConst;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
@Transactional
class AdminAuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("관리자 회원가입 성공")
    void signUpSuccess() throws Exception {
        String requestBody = """
                {
                  "name": "관리자",
                  "email": "cs01@admin.com",
                  "password": "12345678",
                  "phone": "010-1234-5678",
                  "role": "cs"
                }
                """;

        mockMvc.perform(post("/api/admins/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.message").value("관리자 회원가입 신청이 완료되었습니다. 슈퍼 관리자의 승인을 기다려주세요."))
                .andExpect(jsonPath("$.data.name").value("관리자"))
                .andExpect(jsonPath("$.data.email").value("cs01@admin.com"))
                .andExpect(jsonPath("$.data.phone").value("010-1234-5678"))
                .andExpect(jsonPath("$.data.role").value("CS"))
                .andExpect(jsonPath("$.data.status").value("PENDING"));
    }

    @Test
    @DisplayName("중복 이메일로 회원가입하면 실패")
    void signUpFailWhenEmailDuplicated() throws Exception {
        savePendingAdmin(
                "기존관리자",
                "duplicated@admin.com",
                "12345678",
                "010-1111-1111",
                "OPERATION"
        );

        String requestBody = """
                {
                  "name": "관리자",
                  "email": "duplicated@admin.com",
                  "password": "12345678",
                  "phone": "010-1234-5678",
                  "role": "CS"
                }
                """;

        mockMvc.perform(post("/api/admins/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("이미 존재하는 이메일입니다."))
                .andExpect(jsonPath("$.errorCode").value("DUPLICATED_EMAIL"));
    }

    @Test
    @DisplayName("SUPER 역할로 회원가입하면 실패")
    void signUpFailWhenRoleIsSuper() throws Exception {
        String requestBody = """
                {
                  "name": "관리자",
                  "email": "super-test@admin.com",
                  "password": "12345678",
                  "phone": "010-1234-5678",
                  "role": "SUPER"
                }
                """;

        mockMvc.perform(post("/api/admins/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("회원가입 시 SUPER 역할은 선택할 수 없습니다."))
                .andExpect(jsonPath("$.errorCode").value("SUPER_SIGNUP_NOT_ALLOWED"));
    }

    @Test
    @DisplayName("operation 소문자로 회원가입해도 허용")
    void signUpSuccessWhenRoleIsLowerCaseOperation() throws Exception {
        String requestBody = """
                {
                  "name": "운영관리자",
                  "email": "operation01@admin.com",
                  "password": "12345678",
                  "phone": "010-2222-3333",
                  "role": "operation"
                }
                """;

        mockMvc.perform(post("/api/admins/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.role").value("OPERATION"))
                .andExpect(jsonPath("$.data.status").value("PENDING"));
    }

    @Test
    @DisplayName("관리자 로그인 성공 시 세션에 관리자 정보 저장")
    void loginSuccess() throws Exception {
        Admin activeAdmin = saveActiveAdmin(
                "활성관리자",
                "active@admin.com",
                "12345678",
                "010-5555-5555",
                "CS"
        );

        String requestBody = """
                {
                  "email": "active@admin.com",
                  "password": "12345678"
                }
                """;

        mockMvc.perform(post("/api/admins/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("로그인에 성공했습니다."))
                .andExpect(jsonPath("$.data.adminId").value(activeAdmin.getId()))
                .andExpect(jsonPath("$.data.email").value("active@admin.com"))
                .andExpect(jsonPath("$.data.role").value("CS"))
                .andExpect(jsonPath("$.data.status").value("ACTIVE"))
                .andExpect(request().sessionAttribute(SessionConst.ADMIN_ID, activeAdmin.getId()))
                .andExpect(request().sessionAttribute(SessionConst.ADMIN_EMAIL, "active@admin.com"))
                .andExpect(request().sessionAttribute(SessionConst.ADMIN_ROLE, "CS"));
    }

    @Test
    @DisplayName("승인 대기 관리자는 로그인할 수 없다")
    void loginFailWhenStatusIsPending() throws Exception {
        savePendingAdmin(
                "대기관리자",
                "pending@admin.com",
                "12345678",
                "010-6666-6666",
                "CS"
        );

        String requestBody = """
                {
                  "email": "pending@admin.com",
                  "password": "12345678"
                }
                """;

        mockMvc.perform(post("/api/admins/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.message").value("슈퍼 관리자의 승인을 기다리고 있습니다. 승인 후 로그인 부탁드립니다."))
                .andExpect(jsonPath("$.errorCode").value("ADMIN_PENDING"));
    }

    private Admin savePendingAdmin(
            String name,
            String email,
            String rawPassword,
            String phone,
            String roleValue
    ) throws Exception {
        AdminSignUpRequest request = createSignUpRequest(name, email, rawPassword, phone, roleValue);
        AdminRole role = AdminRole.from(request.getRole());
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Admin admin = Admin.signUp(request, role, encodedPassword);
        return adminRepository.save(admin);
    }

    private Admin saveActiveAdmin(
            String name,
            String email,
            String rawPassword,
            String phone,
            String roleValue
    ) throws Exception {
        Admin admin = savePendingAdmin(name, email, rawPassword, phone, roleValue);
        admin.approve();
        return admin;
    }

    private AdminSignUpRequest createSignUpRequest(
            String name,
            String email,
            String password,
            String phone,
            String role
    ) throws Exception {
        String requestBody = """
                {
                  "name": "%s",
                  "email": "%s",
                  "password": "%s",
                  "phone": "%s",
                  "role": "%s"
                }
                """.formatted(name, email, password, phone, role);

        return objectMapper.readValue(requestBody, AdminSignUpRequest.class);
    }
}
