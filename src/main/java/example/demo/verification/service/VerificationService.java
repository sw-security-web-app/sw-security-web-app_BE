package example.demo.verification.service;

import example.demo.config.ResponseDto;
import example.demo.verification.dto.SecurityFileRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface VerificationService {
    //파이썬 서버에 파일 전송
    ResponseDto sendVerificationFileToPythonServer(String token, MultipartFile multipartFile, SecurityFileRequestDto requestDto) throws IOException;
}
