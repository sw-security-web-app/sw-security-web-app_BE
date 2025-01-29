package example.demo.domain.company.api;

import example.demo.domain.company.dto.CompanyCodeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    @GetMapping("/api/companycode")
    public ResponseEntity<?> getCompanyCode(@RequestParam Long memberId){
        //TODO:나중에 JWT토큰으로 변경
        CompanyCodeDto codeDto=companyService.returnCompanyCode(memberId);
        return  ResponseEntity.status(HttpStatus.OK).body(new CompanyCodeDto(codeDto.getCompanyCode()));
    }
}
