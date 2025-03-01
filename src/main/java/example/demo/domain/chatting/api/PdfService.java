package example.demo.domain.chatting.api;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

public interface PdfService {
    ByteArrayResource sendToAiServer(MultipartFile multipartFile,Long companyId);
}
