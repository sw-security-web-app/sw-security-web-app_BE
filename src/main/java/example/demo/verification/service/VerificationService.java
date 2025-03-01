package example.demo.verification.service;

import example.demo.verification.dto.SecurityFileRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface VerificationService {
    //파이썬 서버에 파일 전송
    void sendVerificationFileToPythonServer(String token, MultipartFile multipartFile, SecurityFileRequestDto requestDto) throws IOException;
}
