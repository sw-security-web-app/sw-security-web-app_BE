package example.demo.domain.chatting.api;

import example.demo.domain.chatting.util.PdfConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/pdf")
@RequiredArgsConstructor
public class PdfController {
    private final PdfService pdfService;
    private final PdfConverter pdfConverter=new PdfConverter();

    @PostMapping("/upload")
    public ResponseEntity<?>uploadPdfFile(@RequestHeader("Authorization")String token, @RequestParam("file") MultipartFile multipartFile){
        pdfService.sendToAiServer(token,multipartFile);
        return ResponseEntity.ok().body("서버 업로드 성공");
    }

}
