package example.demo.util.pdf;

import example.demo.verification.util.PdfConverter;
import example.demo.error.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PdfServiceImpl implements PdfService{
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
