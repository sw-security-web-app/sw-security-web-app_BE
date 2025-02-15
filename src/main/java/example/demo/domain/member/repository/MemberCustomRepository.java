package example.demo.domain.member.repository;

import example.demo.domain.member.dto.response.CompanyEmployeeResponseDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface MemberCustomRepository {
    //이메일 중복검사를 위한 이메일 존재 확인
    Long getSameEmailCount(String email);
    //휴대폰 번호 중복검사를 위한 쿼리
    Long getPhoneNumberCount(String phoneNumber);
    //회사Id로 소속 직원 정보 가져오기
    List<CompanyEmployeeResponseDto> getCompanyEmployeeInfo(Long companyId);
}
