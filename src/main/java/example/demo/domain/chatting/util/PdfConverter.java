package example.demo.domain.chatting.util;

import example.demo.util.CreateRandom;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
@Component
public class PdfConverter {

    public ByteArrayResource convertToTxt(MultipartFile multipartFile,Long userId) throws IOException{
        try (PDDocument document=PDDocument.load(multipartFile.getInputStream())){
            ByteArrayOutputStream outputStream=new ByteArrayOutputStream();

            PDFTextStripper pdfTextStripper=new PDFTextStripper();
            String extractedText=pdfTextStripper.getText(document);

            outputStream.write(extractedText.getBytes(StandardCharsets.UTF_8));

            return new ByteArrayResource(outputStream.toByteArray()){
                @Override
                public String getFilename(){
                    return makeFileName(userId);
                }
            };
        }
    }

    //파일 이름 생성 랜덤 Uuid_유저id값
    private String makeFileName(Long userId){
        String uuid= CreateRandom.createShortUuid();
        return uuid+"_"+userId+".txt";
    }
}
