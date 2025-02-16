package example.demo.domain.member.api;

import example.demo.data.RedisCustomService;
import example.demo.domain.company.Company;
import example.demo.domain.company.CompanyErrorCode;
import example.demo.domain.company.dto.response.CompanyResponseDto;
import example.demo.domain.company.repository.CompanyRepository;
import example.demo.domain.company.dto.CompanyInfoWithUuidDto;
import example.demo.domain.member.Member;
import example.demo.domain.member.MemberErrorCode;
import example.demo.domain.member.MemberStatus;
import example.demo.domain.member.dto.response.CompanyEmployeeResponseDto;
import example.demo.security.auth.dto.MemberLoginDto;
import example.demo.domain.member.repository.MemberRepository;
import example.demo.domain.member.dto.request.MemberRequestDto;
import example.demo.error.RestApiException;
import example.demo.security.util.JwtUtil;
import example.demo.util.CreateRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    //TODO
    //일반 -> 회사이름,직책,부서명 / 초대코드 없음.
    //관리자 -> 회사이름, 직책, 부서명 있어야함 / 초대코드는 없음.
    //직원 -> 초대코드로 회사이름,부서 추출 / 직책은 사용자 입력.

    //TODO : 비밀번호 암호화 및 휴대폰,이메일 인증 여부 확인 로직 구현
    //TODO:JWT Filter로 관리자 확인 로직 구현
    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

   // private final SmsCertificationDao smsCertificationDao;
    private final RedisCustomService redisCustomService;


    //회원가입 이전 : 이메일 인증, 휴대폰 인증 여부 확인.
    public void signup(MemberRequestDto memberRequestDto){
        Member newMember ;
        Company company;
        //이메일 중복, 휴대폰번호 중복 예외처리는 해당 서비스 계층에서 실시합니다.
        //회원가입 전 이메일 인증 및 휴대폰 번호 인증 여부
        if(smsAndMailValidation(memberRequestDto.getEmail(),memberRequestDto.getPhoneNumber())){
            throw new RestApiException(MemberErrorCode.INVALID_CERTIFICATION_EMAIL_OR_PHONE);
        }

        switch (memberRequestDto.getMemberStatus().toLowerCase()){
            //일반
            case "general":
                newMember=Member.createGeneral(memberRequestDto);
                break;
            //관리자
            case "manager":
                //uuid생성
                String companyCode= CreateRandom.createShortUuid();
                company=companyRepository.findByCompanyNameAndCompanyDept(memberRequestDto.getCompanyName(),memberRequestDto.getCompanyDept())
                                .orElse(
                                        Company.builder()
                                                .companyName(memberRequestDto.getCompanyName())
                                                .companyDept(memberRequestDto.getCompanyDept())
                                                .invitationCode(companyCode)
                                                .build()
                                );
                companyRepository.save(company);
                newMember=Member.createManager(memberRequestDto,company);
                break;
            //직원
            case "employee":
                //uuid로 회사명, 회사부서명 가져오기
                CompanyInfoWithUuidDto companyInfoWithUuidDto=companyRepository.findCompanyInfoByInvitationCode(memberRequestDto.getInvitationCode());
                String companyName=companyInfoWithUuidDto.getCompanyName();
                String companyDept=companyInfoWithUuidDto.getCompanyDept();
                company=companyRepository.findByCompanyNameAndCompanyDept(companyName,companyDept)
                                .orElseThrow(()->new RestApiException(CompanyErrorCode.NOT_EXIST_COMPANY));

                newMember=Member.createEmployee(memberRequestDto,company);
                break;
            default:
                throw new RestApiException(MemberErrorCode.INVALID_MEMBER_STATUS);
        }
        //비밀번호 암호화
        newMember.setPassword(passwordEncoder.encode(newMember.getPassword()));
        memberRepository.save(newMember);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompanyEmployeeResponseDto>  getAllEmployees(String token, Pageable page) {
        Member member=memberRepository.findById(jwtUtil.getMemberId(token))
                .orElseThrow(()->new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));
        //MANAGER인지 판단
        if(!member.getMemberStatus().equals(MemberStatus.MANAGER)){
            throw new RestApiException(MemberErrorCode.INVALID_PERMISSION);
        }
        //DTO조립
        return memberRepository.getCompanyEmployeeInfo(member.getCompany().getCompanyId(),page);
    }


    private boolean smsAndMailValidation(String email,String phoneNumber){
        if (isTestMode()) {
            return false; // 테스트 모드일 때 항상 false
        }
        String VALIDATION_PREFIX = "cer: ";
        return !((redisCustomService.hasKey(VALIDATION_PREFIX +email)&&
                    redisCustomService.hasKey(VALIDATION_PREFIX +phoneNumber)&&
                        redisCustomService.getRedisData(VALIDATION_PREFIX +email).equals("TRUE")&&
                            redisCustomService.getRedisData(VALIDATION_PREFIX +phoneNumber).equals("TRUE")
        ));
    }

    //test를 위한 메서드
    private boolean isTestMode() {
        return Boolean.parseBoolean(System.getProperty("test.mode", "false"));
    }

}
