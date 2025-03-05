package example.demo.verification.controller;

import example.demo.config.ResponseDto;
import example.demo.error.RestApiException;
import example.demo.verification.dto.SecurityFileRequestDto;
import example.demo.verification.error.VerificationErrorCode;
import example.demo.verification.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class VerificationController {
    private final VerificationService verificationService;
    @PostMapping(value = "company-send",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> postCensorshipData(@RequestHeader("Authorization")String token,
                                                @RequestPart(value = "requestDto",required = false) SecurityFileRequestDto requestDto,
                                                @RequestPart(value = "file",required = false) MultipartFile file) throws IOException, JSONException {
        //파일이나 텍스트가 없는 경우
        if(requestDto==null && (file!=null &&  file.isEmpty())){
            throw new RestApiException(VerificationErrorCode.EMPTY_FILE_OR_TEXT);
        }
        ResponseDto responseDto=verificationService.sendVerificationFileToPythonServer(token,file,requestDto);
        return ResponseEntity.ok().body(responseDto);
    }
}
