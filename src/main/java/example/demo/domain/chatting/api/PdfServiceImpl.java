package example.demo.domain.chatting.api;

import example.demo.domain.chatting.util.PdfConverter;
import example.demo.error.RestApiException;
import example.demo.security.util.JwtUtil;
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

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PdfServiceImpl implements PdfService{
    @Value("${ai.server.url}")
    private String aiServerUrl;

    private final JwtUtil jwtUtil;
    private final PdfConverter pdfConverter=new PdfConverter();
    private static final long maxSize=30*1024*1024;
    @Override
    public ByteArrayResource sendToAiServer(MultipartFile multipartFile,Long companyId) {

        //파일 크기 초과
        if(multipartFile.getSize()>maxSize){
            throw new RestApiException(PdfErrorCode.FILE_SIZE_OVER);
        }

        //pdf->txt 변환
        ByteArrayResource txtFile;
        try {
            txtFile=pdfConverter.convertToTxt(multipartFile,companyId);
        }catch (IOException e){
            throw new RestApiException(PdfErrorCode.CONVERSION_FAILED);
        }
        return txtFile;
    }


}
