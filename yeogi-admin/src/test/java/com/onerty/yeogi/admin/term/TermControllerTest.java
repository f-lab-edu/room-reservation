package com.onerty.yeogi.admin.term;

import com.onerty.yeogi.common.security.JwtTokenProvider;
import com.onerty.yeogi.common.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TermController.class)
@Import(SecurityConfig.class)
class TermControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private TermService termService;

    private final String REGISTER_TERM_JSON = """
            {
                "title": "PRIVACY_POLICY",
                "isRequired": true,
                "content": "개인정보 보호 약관입니다."
            }
            """;

    private final String UPDATE_TERM_JSON = """
            {
                "title": "PRIVACY_POLICY",
                "content": "개인정보 보호 약관이 변경되었습니다."
            }
            """;

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void ADMIN_권한으로_약관_등록_성공() throws Exception {
        mockMvc.perform(post("/admin/api/terms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REGISTER_TERM_JSON))
                .andExpect(status().isOk()); // 권한이 맞으면 OK
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void ADMIN_권한으로_약관_수정_성공() throws Exception {
        mockMvc.perform(patch("/admin/api/terms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UPDATE_TERM_JSON))
                .andExpect(status().isOk());
    }

    // CUSTOMER, 비회원 접근 시 403 forbidden
    @Test
    @WithMockUser(username = "user", authorities = {"CUSTOMER"})
    void USER_권한으로_약관_등록_실패() throws Exception {
        mockMvc.perform(post("/admin/api/terms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REGISTER_TERM_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"CUSTOMER"})
    void USER_권한으로_약관_수정_실패() throws Exception {
        mockMvc.perform(patch("/admin/api/terms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UPDATE_TERM_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void 익명_사용자가_약관_등록_시_403_반환() throws Exception {
        mockMvc.perform(post("/admin/api/terms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REGISTER_TERM_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void 익명_사용자가_약관_수정_시_403_반환() throws Exception {
        mockMvc.perform(patch("/admin/api/terms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UPDATE_TERM_JSON))
                .andExpect(status().isForbidden());
    }
}
