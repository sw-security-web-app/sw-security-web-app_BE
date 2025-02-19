package example.demo.domain.member.api;

import example.demo.data.RedisCustomService;
import example.demo.domain.member.dto.response.MemberInfoResponseDto;
import example.demo.security.auth.CustomMemberDetailService;
import example.demo.security.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private MemberService memberService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private CustomMemberDetailService customMemberDetailService;
    @MockitoSpyBean
    private RedisCustomService redisCustomService;


    @Test
    @DisplayName("[GET]회원 정보를 조회합니다.")
    void getInfo() throws Exception{
        //given
        String token="valid";
        MemberInfoResponseDto expectedResponse = MemberInfoResponseDto.builder()
                .name("김도연")
                .email("tkv00@naver.com")
                .companyName("삼성")
                .companyDept("개발부")
                .companyPosition("사장")
                .build();
        when(memberService.getMemberInfo(token)).thenReturn(expectedResponse);

        //when&then
        mvc.perform(get("/api/my-info")
                .header("token",token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("김도연"))
                .andExpect(jsonPath("$.email").value("tkv00@naver.com"))
                .andExpect(jsonPath("$.companyName").value("삼성"))
                .andExpect(jsonPath("$.companyDept").value("개발부"))
                .andExpect(jsonPath("$.companyPosition").value("사장"));
    }


}