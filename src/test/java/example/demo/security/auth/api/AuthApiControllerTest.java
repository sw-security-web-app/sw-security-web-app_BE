package example.demo.security.auth.api;

import example.demo.security.auth.dto.ChangePasswordRequestDto;
import kotlinx.serialization.json.Json;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
@SpringBootTest
@AutoConfigureMockMvc
class AuthApiControllerTest {

    @MockitoBean
    private AuthService authService;

    @Autowired
    private AuthApiController authApiController;

    @Autowired
    private MockMvc mvc;
    @Test
    @DisplayName("[PUT]-비밀번호 성공 시 ResponseDto를 반환합니다.")
    void changePassword() throws Exception {
        //given
        String token="token";
        ChangePasswordRequestDto requestDto=ChangePasswordRequestDto
                .builder()
                .password("tkv0123")
                .newPassword("tkv00@@@!!")
                .email("tkv00@naver.com")
                .build();

        doNothing().when(authService).changePassword(eq(token),eq(requestDto));
        //when
        //then
        mvc.perform(put("/api/auth/change-password")
                .header("Authorization",token)
                        .contentType("application/json")
                        .content("{\"email\": \"tkv00@naver.com\", \"password\": \"tkv0123!!\", \"newPassword\": \"tkv99@@@\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("비밀번호 변경 성공"));

    }
}