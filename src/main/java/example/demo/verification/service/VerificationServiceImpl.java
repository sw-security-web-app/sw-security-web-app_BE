package example.demo.verification.service;

import example.demo.config.ResponseDto;
import example.demo.error.CommonErrorCode;
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
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerificationServiceImpl implements VerificationService {
    @Value("${python.server.url}")
    private String SERVER_URL;
    //허용 파일 타입
    private static final String[] fileType = {"txt", "csv", "pdf"};
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final PdfService pdfService;

    @Override
    public ResponseDto sendVerificationFileToPythonServer(String token, MultipartFile multipartFile, SecurityFileRequestDto requestDto) throws IOException, JSONException {
        //파일이나 텍스트가 없는 경우
        if ((requestDto == null || requestDto.getContent() == null || requestDto.getContent().isEmpty())
                && (multipartFile == null || multipartFile.isEmpty())) {
            throw new RestApiException(VerificationErrorCode.EMPTY_FILE_OR_TEXT);
        }
        //유저가 관리자 인지 검사
        Long companyId = isMemberManager(token);

        /*회사 생성 이전 회사 존재 여부 판단*/
        //존재하지 않는 경우
        if (!isExistCompanyFile(companyId)){
            //회사 생성
            try {
                createCompany(companyId);
                log.info("Success Create Company File");
            }catch (ResourceAccessException e){
                //회사 생성 시간 초과
                log.error("Time Out!");
                throw new RestApiException(VerificationErrorCode.TIME_OUT_ERROR);
            }catch (RestClientException e){
                //회사 생성 중 통신 오류
                log.error(e.toString());
                throw new  RestApiException(VerificationErrorCode.ERROR_OF_SEND_FILE_COMPANY);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        //회사가 존재하는 경우 -> 바로 학습 api 전송
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        JSONObject json=new JSONObject();
        json.put("company_name", String.valueOf(companyId));



        //파일 타입 유형 검사
        if (!multipartFile.isEmpty()) {
            String extractFileName = multipartFile.getOriginalFilename();
            if (Arrays.stream(fileType).noneMatch(
                    file -> file.equals(extractFileType(extractFileName)))) {
                throw new RestApiException(VerificationErrorCode.NO_FILE_TYPE);
            }

            //pdf 파일인 경우 -> .txt로 변환
            //TODO: pdf파일 컨버터 사용
            if (extractFileType(extractFileName).equals("pdf")) {
                ByteArrayResource convertedPdfFile = pdfService.sendToAiServer(multipartFile, companyId);
                log.info("PDF Converter 실행");
                if(convertedPdfFile==null){
                    throw new RestApiException(VerificationErrorCode.ERROR_OF_CONVERTING_PDF);
                }
                log.info("PDF -> .txt 변환 성공!");
                body.add("file", convertedPdfFile);
            } else {
                ByteArrayResource fileResource = new ByteArrayResource(multipartFile.getBytes()) {
                    @NotNull
                    @Override
                    public String getFilename() {
                        return multipartFile.getOriginalFilename();
                    }
                };
                log.info("File !! "+fileResource);
                body.add("file", fileResource);
            }

        }
        //텍스트만 존재하는 경우
        //TODO
        else {
            body.add("text", requestDto.getContent());
        }
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        body.add("company_name", json.toString());

        SimpleClientHttpRequestFactory factory=new SimpleClientHttpRequestFactory();
        RestTemplate restTemplate=new RestTemplate(factory);

        factory.setConnectTimeout(Duration.ofMinutes(10));//연결 시간 10분
        factory.setReadTimeout(Duration.ofMinutes(10));   //읽기 시간 10분

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body,headers);
        ResponseEntity<String> response;

        try {
            response=restTemplate.postForEntity(SERVER_URL + "/api/" + companyId + "/train"
                    , requestEntity
                    , String.class
            );
        }catch (ResourceAccessException e){
            //요청시간 초과 시
            log.error(e.toString());
            throw new RestApiException(VerificationErrorCode.TIME_OUT_ERROR);
        }catch (RestClientException e){
            //요청 중 오류 발생 시
            /*
            * 여기서 오류 발생
            * */
            log.error(e.toString());
            throw new RestApiException(VerificationErrorCode.ERROR_OF_SEND_FILE_COMPANY);
        }

        /*
         * 전송 성공 유무 처리
         * 성공 - 200 실패 - 422
         * */
        if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
            return ResponseDto.of(response.getStatusCode().value(),response.getBody());
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
        log.info(originalFileName.substring(pos+1));
        return originalFileName.substring(pos + 1);
    }
    //파이썬 서버 회사 생성
    private void createCompany(Long companyId) throws JSONException {
        JSONObject json=new JSONObject();
        json.put("company_name", String.valueOf(companyId));

        //요청 만들기
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(json.toString(),headers);
        SimpleClientHttpRequestFactory factory=new SimpleClientHttpRequestFactory();
        RestTemplate restTemplate=new RestTemplate(factory);

        factory.setConnectTimeout(Duration.ofSeconds(30));//연결 시간 30초
        factory.setReadTimeout(Duration.ofSeconds(30));   //읽기 시간 30초

        String url = SERVER_URL + "/api/create_company";
        log.info("Send File Request");
        log.info(request.getBody());

        try {
            restTemplate.postForEntity(url, request, String.class);
            log.info("✅Create Company Success");
        }catch (HttpServerErrorException e){
            log.error(e.getMessage());
            throw new RestApiException(VerificationErrorCode.ERROR_OF_SEND_FILE_COMPANY);
        }
    }


    //이미 회사 파일이 존재하는 지 검사
    private boolean isExistCompanyFile(Long companyId) throws JSONException {
        JSONObject json=new JSONObject();
        json.put("company_name", String.valueOf(companyId));

        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(json.toString(),headers);
        SimpleClientHttpRequestFactory factory=new SimpleClientHttpRequestFactory();
        RestTemplate restTemplate=new RestTemplate(factory);

        factory.setConnectTimeout(Duration.ofSeconds(15));//연결 시간 10초
        factory.setReadTimeout(Duration.ofSeconds(15));   //읽기 시간10초

        //회사가 존재하면 true 반환
        String url=SERVER_URL+"/api/"+companyId+"/info";

        try {
            ResponseEntity<String>response= restTemplate.postForEntity(url, request, String.class);
            log.info("✅Is Exist CompanyFile !! "+response.getBody());
            return response.getStatusCode().is2xxSuccessful();
        }catch (HttpClientErrorException.NotFound e){
            return false;
        }catch (RestClientException e){
            throw new RestApiException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }

    }
}
