package example.demo.domain.company.api;

import example.demo.domain.company.dto.CompanyCodeDto;
import example.demo.domain.company.dto.request.SecurityFileRequestDto;
import example.demo.domain.company.dto.response.CompanyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CompanyController {
    private final CompanyService companyService;
    @GetMapping("/company-code")
    public ResponseEntity<?> getCompanyCode(@RequestHeader("Authorization")String token){
        //TODO:나중에 JWT토큰으로 변경
        CompanyCodeDto codeDto=companyService.returnCompanyCode(token);
        return  ResponseEntity.status(HttpStatus.OK).body(new CompanyCodeDto(codeDto.getCompanyCode()));
    }

    @GetMapping("/company-info")
    public ResponseEntity<?> getCompanyInfo(@RequestHeader("Authorization")String token){
        CompanyResponseDto companyInfo = companyService.getCompanyInfo(token);
        return ResponseEntity.ok(companyInfo);
    }

    @PostMapping("/company-send")
    public ResponseEntity<?> postCensorshipData(@RequestHeader("Authorization")String token,
                                                @RequestPart(value = "requestDto",required = false) SecurityFileRequestDto requestDto,
                                                @RequestPart(value = "file",required = false)MultipartFile file){
        return ResponseEntity.ok().body()
    }
}
