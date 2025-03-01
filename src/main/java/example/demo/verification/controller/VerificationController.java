package example.demo.verification.controller;

import example.demo.config.ResponseDto;
import example.demo.error.RestApiException;
import example.demo.verification.dto.SecurityFileRequestDto;
import example.demo.verification.error.VerificationErrorCode;
import example.demo.verification.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class VerificationController {
    private final VerificationService verificationService;
    @PostMapping("/company-send")
    public ResponseEntity<?> postCensorshipData(@RequestHeader("Authorization")String token,
                                                @RequestPart(value = "requestDto",required = false) SecurityFileRequestDto requestDto,
                                                @RequestPart(value = "file",required = false) MultipartFile file) throws IOException {
        //파일이나 텍스트가 없는 경우
        if(requestDto==null || file.isEmpty()){
            throw new RestApiException(VerificationErrorCode.EMPTY_FILE_OR_TEXT);
        }
        verificationService.sendVerificationFileToPythonServer(token,file,requestDto);
        return ResponseEntity.ok().body(ResponseDto.of(200,"전송 성공"));
    }
}
