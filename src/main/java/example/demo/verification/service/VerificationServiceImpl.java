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
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.io.IOException;
import java.time.Duration;
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
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final PdfService pdfService;
    private final WebClient webClient=WebClient.builder()
            .baseUrl(SERVER_URL)
            .clientConnector(new ReactorClientHttpConnector(
                    HttpClient.create()
                            .responseTimeout(Duration.ofSeconds(10))//응답 시간 10초
            ))
            .build();



    @Override
    public ResponseDto sendVerificationFileToPythonServer(String token, MultipartFile multipartFile, SecurityFileRequestDto requestDto) throws IOException {
        //유저가 관리자 인지 검사
        Long companyId = isMemberManager(token);

        //회사 생성
        ResponseEntity<?> response ;
        try {
            response=createCompany(companyId);
        }catch (ResourceAccessException e){
            //회사 생성 시간 초과
            throw new RestApiException(VerificationErrorCode.TIME_OUT_ERROR);
        }catch (RestClientException e){
            //회사 생성 중 통신 오류
            throw new RestApiException(VerificationErrorCode.ERROR_OF_CREATE_COMPANY);
        }

        //회사 생성 중 AI서버에서 오류 발생인 경우 예외처리
        if (response.getStatusCode().is4xxClientError()) {
            throw new RestApiException(VerificationErrorCode.ERROR_OF_CREATE_COMPANY);
        }

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        String extractFileName = multipartFile.getOriginalFilename();
        body.add("company_name", companyId);

        insertDataByFileType(multipartFile, requestDto, extractFileName, companyId, body);

        SimpleClientHttpRequestFactory factory=new SimpleClientHttpRequestFactory();
        RestTemplate restTemplate=new RestTemplate(factory);

        factory.setConnectTimeout(5000);//연결 시간 5초
        factory.setReadTimeout(5000);   //읽기 시간5초

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body);

        ResponseEntity<String> request ;

        try {
            request=restTemplate.postForEntity(SERVER_URL + "/" + companyId + "/train"
                    , requestEntity
                    , String.class
            );
        }catch (ResourceAccessException e){
            //요청시간 초과 시
            throw new RestApiException(VerificationErrorCode.TIME_OUT_ERROR);
        }catch (RestClientException e){
            //요청 중 오류 발생 시
            throw new RestApiException(VerificationErrorCode.ERROR_OF_SEND_FILE_COMPANY);
        }

        /*
         * 전송 성공 유무 처리
         * 성공 - 200 실패 - 422
         * */
        if (request.getStatusCode().is4xxClientError()) {
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

        //요청 만들기
        HttpEntity<Map<String, String>> request = new HttpEntity<>(json);
        SimpleClientHttpRequestFactory factory=new SimpleClientHttpRequestFactory();
        RestTemplate restTemplate=new RestTemplate(factory);

        factory.setConnectTimeout(5000);//연결 시간 5초
        factory.setReadTimeout(5000);   //읽기 시간5초

        String url = SERVER_URL + "/create_company";
        return restTemplate.postForEntity(url, request, String.class);
    }

    //파일 타입별 body조립 메서드
    private void insertDataByFileType(MultipartFile multipartFile, SecurityFileRequestDto requestDto, String extractFileName, Long companyId, MultiValueMap<String, Object> body) throws IOException {
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
    }

}
