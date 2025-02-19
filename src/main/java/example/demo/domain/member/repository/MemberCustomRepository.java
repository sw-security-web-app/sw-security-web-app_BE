package example.demo.domain.member.repository;

import example.demo.domain.member.dto.response.MemberInfoResponseDto;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface MemberCustomRepository {
    //이메일 중복검사를 위한 이메일 존재 확인
    Long getSameEmailCount(String email);
    //휴대폰 번호 중복검사를 위한 쿼리
    Long getPhoneNumberCount(String phoneNumber);
    //회원의 마이페이지 추출을 위한 쿼리
    MemberInfoResponseDto getMemberInfo(Long memberId);
}
