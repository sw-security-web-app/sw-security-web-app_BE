package example.demo.verification.service;

import example.demo.domain.company.repository.CompanyRepository;
import example.demo.domain.member.Member;
import example.demo.domain.member.MemberErrorCode;
import example.demo.domain.member.MemberStatus;
import example.demo.domain.member.repository.MemberRepository;
import example.demo.error.CommonErrorCode;
import example.demo.error.RestApiException;
import example.demo.security.util.JwtUtil;
import example.demo.verification.dto.SecurityFileRequestDto;
import example.demo.verification.error.VerificationErrorCode;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {
    @Value("${python.server.url}")
    private static String SERVER_URL;
    //허용 파일 타입
    private static final String[] fileType = {".txt", ".csv", ".pdf"};
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;


    @Override
    public void sendVerificationFileToPythonServer(String token, MultipartFile multipartFile, SecurityFileRequestDto requestDto) throws IOException {
        //유저가 관리자 인지 검사
        Long companyId=isMemberManager(token);

        HttpHeaders headers=new org.springframework.http.HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String,Object> body=new LinkedMultiValueMap<>();

        //파일 타입 유형 검사
        if (!multipartFile.isEmpty()) {
            if (Arrays.stream(fileType).noneMatch(
                    file -> file.equals(extractFileType(multipartFile.getOriginalFilename())))) {
                throw new RestApiException(VerificationErrorCode.NO_FILE_TYPE);
            }

            //pdf 파일인 경우 -> .txt로 변환
            //TODO: pdf파일 컨버터 사용
            ByteArrayResource fileResource=new ByteArrayResource(multipartFile.getBytes()){
                @NotNull
                @Override
                public String getFilename(){
                    return multipartFile.getOriginalFilename();
                }
            };
            body.add("file",fileResource);


        }
        //텍스트만 존재하는 경우
        else {
            body.add("text",requestDto.getContent());
        }

        HttpEntity<MultiValueMap<String,Object>> requestEntity=new HttpEntity<>(body);

        ResponseEntity<String> response=restTemplate.postForEntity(SERVER_URL+"/api/create_company"
                ,requestEntity
                ,String.class
        );

    }

    //유저가 관리자 인지 검사 + 회사 인덱스 반환 메서드
    private Long isMemberManager(String token) {
        Long memberId=jwtUtil.getMemberId(token);
        Member member=memberRepository.findById(memberId)
                .orElseThrow(()->new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));
        if(!member.getMemberStatus().equals(MemberStatus.MANAGER)){
            throw new RestApiException(MemberErrorCode.INVALID_PERMISSION);
        }

        return member.getCompany().getCompanyId();
    }

    //확장자 명 추출 함수
    private String extractFileType(String originalFileName) {
        int pos = originalFileName.lastIndexOf(".");
        return originalFileName.substring(pos + 1);
    }


}
