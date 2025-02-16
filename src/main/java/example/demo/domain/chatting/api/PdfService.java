package example.demo.domain.chatting.api;

import org.springframework.web.multipart.MultipartFile;

public interface PdfService {
    String sendToAiServer(String token, MultipartFile multipartFile);
}
