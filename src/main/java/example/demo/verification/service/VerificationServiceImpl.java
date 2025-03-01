package example.demo.verification.service;

import example.demo.config.ResponseDto;
import example.demo.util.pdf.PdfService;
import example.demo.domain.member.Member;
import example.demo.domain.member.MemberErrorCode;
import example.demo.domain.member.MemberStatus;
import example.demo.domain.member.repository.MemberRepository;
import example.demo.error.RestApiException;
import example.demo.security.util.JwtUtil;
import example.demo.verification.dto.SecurityFileRequestDto;
import example.demo.verification.error.VerificationErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerificationServiceImpl implements VerificationService {
    @Value("${python.server.url}")
    private String SERVER_URL;
    //허용 파일 타입
    private static final String[] fileType = {".txt", ".csv", ".pdf"};
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final PdfService pdfService;


    @Override
    public ResponseDto sendVerificationFileToPythonServer(String token, MultipartFile multipartFile, SecurityFileRequestDto requestDto) throws IOException {
        //유저가 관리자 인지 검사
        Long companyId = isMemberManager(token);

        //회사 생성
        ResponseEntity<?> response = createCompany(companyId);

        //회사 생성 중 AI서버에서 오류 발생인 경우 예외처리
        if (response.getStatusCode().is4xxClientError()) {
            throw new RestApiException(VerificationErrorCode.ERROR_OF_CREATE_COMPANY);
        }

        HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        String extractFileName = multipartFile.getOriginalFilename();
        body.add("company_name", companyId);

        //파일 타입 유형 검사
        if (!multipartFile.isEmpty()) {
            if (Arrays.stream(fileType).noneMatch(
                    file -> file.equals(extractFileType(extractFileName)))) {
                throw new RestApiException(VerificationErrorCode.NO_FILE_TYPE);
            }

            //pdf 파일인 경우 -> .txt로 변환
            //TODO: pdf파일 컨버터 사용
            if (extractFileType(extractFileName).equals(".pdf")) {
                ByteArrayResource convertedPdfFile = pdfService.sendToAiServer(multipartFile, companyId);
                body.add("file", convertedPdfFile);
            } else {
                ByteArrayResource fileResource = new ByteArrayResource(multipartFile.getBytes()) {
                    @NotNull
                    @Override
                    public String getFilename() {
                        return multipartFile.getOriginalFilename();
                    }
                };
                body.add("file", fileResource);
            }

        }
        //텍스트만 존재하는 경우
        else {
            body.add("text", requestDto.getContent());
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body);

        ResponseEntity<String> request = restTemplate.postForEntity(SERVER_URL + "/" + companyId + "/train"
                , requestEntity
                , String.class
        );

        /*
         * 전송 성공 유무 처리
         * 성공 - 200 실패 - 422
         * */
        if (request.getStatusCode().equals(422)) {
            throw new RestApiException(VerificationErrorCode.ERROR_OF_CREATE_COMPANY);
        }
        return ResponseDto.of(200, "파일을 성공적으로 업로드하였습니다.");
    }

    //유저가 관리자 인지 검사 + 회사 인덱스 반환 메서드
    private Long isMemberManager(String token) {
        Long memberId = jwtUtil.getMemberId(token);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));
        if (!member.getMemberStatus().equals(MemberStatus.MANAGER)) {
            throw new RestApiException(MemberErrorCode.INVALID_PERMISSION);
        }

        return member.getCompany().getCompanyId();
    }

    //확장자 명 추출 함수
    private String extractFileType(String originalFileName) {
        int pos = originalFileName.lastIndexOf(".");
        return originalFileName.substring(pos + 1);
    }

    //파이썬 서버 회사 생성
    private ResponseEntity<?> createCompany(Long companyId) {
        Map<String, String> json = new HashMap<>();
        json.put("company_name", String.valueOf(companyId));

        HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.APPLICATION_JSON);

        //요청 만들기
        HttpEntity<Map<String, String>> request = new HttpEntity<>(json);
        String url = SERVER_URL + "/create_company";
        log.info(url);
        return restTemplate.postForEntity(url, request, String.class);
    }

}
