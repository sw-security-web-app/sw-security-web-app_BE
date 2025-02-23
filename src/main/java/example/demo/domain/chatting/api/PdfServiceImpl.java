package example.demo.domain.chatting.api;

import example.demo.domain.chatting.util.PdfConverter;
import example.demo.domain.member.MemberStatus;
import example.demo.error.RestApiException;
import example.demo.security.util.JwtUtil;
import example.demo.util.CreateRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PdfServiceImpl implements PdfService{
    @Value("${ai.server.url}")
    private String aiServerUrl;

    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate=new RestTemplate();
    private final PdfConverter pdfConverter=new PdfConverter();
    private static final long maxSize=30*1024*1024;
    @Override
    public String sendToAiServer(String token, MultipartFile multipartFile) {

        //업로드 권한 확인
        if(!jwtUtil.getMemberStatus(token).equals("MANAGER")){
            throw new RestApiException(PdfErrorCode.INVALID_PERMISSION);
        }
        //파일 크기 초과
        if(multipartFile.getSize()>maxSize){
            throw new RestApiException(PdfErrorCode.FILE_SIZE_OVER);
        }
        Long memberId= jwtUtil.getMemberId(token);
        //pdf->txt 변환
        ByteArrayResource txtFile;
        try {
            txtFile=pdfConverter.convertToTxt(multipartFile,memberId);
        }catch (IOException e){
            throw new RestApiException(PdfErrorCode.CONVERSION_FAILED);
        }

        //AI 서버 전송
        String url=aiServerUrl+"/process";
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultipartBodyBuilder builder=new MultipartBodyBuilder();
        builder.part("file",txtFile)
                .header("Content-Disposition","form-data; name=file; filename="+txtFile.getFilename());
        HttpEntity<?> request=new HttpEntity<>(builder.build(),headers);

        return restTemplate.postForObject(url,request,String.class);
    }


}
