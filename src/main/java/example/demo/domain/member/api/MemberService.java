package example.demo.domain.member.api;

import example.demo.domain.company.CompanyRepository;
import example.demo.domain.member.Member;
import example.demo.domain.member.MemberRepository;
import example.demo.domain.member.dto.request.MemberRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    //TODO
    //일반 -> 회사이름,직책,부서명 / 초대코드 없음.
    //관리자 -> 회사이름, 직책, 부서명 있어야함 / 초대코드는 없음.
    //직원 -> 초대코드로 회사이름,부서 추출 / 직책은 사용자 입력.
    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;

    public void signUp(MemberRequestDto memberRequestDto,String memberStatus){
        //일반

        //관리자

        //직원
    }

}
