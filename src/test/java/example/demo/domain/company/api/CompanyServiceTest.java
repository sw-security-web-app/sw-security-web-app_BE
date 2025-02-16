package example.demo.domain.company.api;

import example.demo.domain.company.Company;
import example.demo.domain.company.dto.response.CompanyResponseDto;
import example.demo.domain.company.repository.CompanyRepository;
import example.demo.domain.member.Member;
import example.demo.domain.member.dto.request.MemberRequestDto;
import example.demo.domain.member.repository.MemberRepository;
import example.demo.error.RestApiException;
import example.demo.security.util.JwtUtil;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@SpringBootTest
class CompanyServiceTest {
    @MockitoBean
    private CompanyRepository companyRepository;
    @MockitoBean
    private MemberRepository memberRepository;
    @MockitoBean
    private JwtUtil jwtUtil;
    @Autowired
    private CompanyService companyService;


    @Test
    @DisplayName("성공케이스-회원Id로 회원의 회사정보를 조회합니다.")
    void getCompanyInfo(){
        //given
        String TOKEN="TOKEN";
        Long memberId=2L;

        CompanyResponseDto companyResponseDto=new CompanyResponseDto("삼성","개발");

        when(jwtUtil.getMemberId(TOKEN)).thenReturn(memberId);
        when(companyRepository.getCompanyInfo(memberId)).thenReturn(companyResponseDto);

        //when
        CompanyResponseDto actualResponse=companyService.getCompanyInfo(TOKEN);

        //then
        verify(companyRepository,times(1)).getCompanyInfo(memberId);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getCompanyDept()).isEqualTo(companyResponseDto.getCompanyDept());
        assertThat(actualResponse.getCompanyName()).isEqualTo(companyResponseDto.getCompanyName());
    }

    @Test
    @DisplayName("실패케이스-회사Id가 없으면 예외를 반환합니다.")
    void getCompanyInfo_Fail(){
        //given
        String TOKEN="TOKEN";
        Long memberId=1L;

        when(jwtUtil.getMemberId(TOKEN)).thenReturn(memberId);
        when(companyRepository.getCompanyInfo(memberId)).thenReturn(null);

        //when
        org.junit.jupiter.api.Assertions.assertThrows(RestApiException.class,()->{
            companyService.getCompanyInfo(TOKEN);
        });

        //then
        verify(jwtUtil).getMemberId(TOKEN);
        verify(companyRepository).getCompanyInfo(memberId);
    }
}