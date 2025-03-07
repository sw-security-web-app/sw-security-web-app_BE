package example.demo.verification.util;

import example.demo.domain.member.MemberStatus;
import example.demo.error.RestApiException;
import example.demo.util.UtilErrorCode;
import example.demo.verification.service.VerificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.web.reactive.function.client.WebClient.*;

@Component
@Slf4j
public class PythonServerUtil {

    private final WebClient webClient;
    private final VerificationService verificationService;

    @Value("${python.server.url}")
    private String pythonServerUrl;

    public PythonServerUtil(Builder webClient,VerificationService verificationService) {
        this.webClient = webClient.build();
        this.verificationService=verificationService;
    }

    public void validatePrompt(String prompt, Long companyId) {
        Map<String, String> requestBody = Map.of("prompt", prompt);
        HttpStatusCode responseStatus = null;

        //일반인
        if (companyId.equals(0L)) {
            responseStatus=webClient.post()
                    .uri(pythonServerUrl +  "/predict")
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .exchangeToMono(response -> Mono.just(response.statusCode()))
                    .block();
        }else {
            //회사 AI가 생성되었는지 확인
            verificationService.checkIsAiCreateByCompanyId(companyId);

            responseStatus=webClient.post()
                    .uri(pythonServerUrl + "/api/" + companyId + "/predict")
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .exchangeToMono(response -> Mono.just(response.statusCode()))
                    .block();
        }

        if (responseStatus == null) {
            throw new RestApiException(UtilErrorCode.PYTHON_SERVER_ERROR);
        } else if (responseStatus.isSameCodeAs(HttpStatus.BAD_REQUEST)) {
            log.error("❌회사 보안 위반!❌");
            throw new RestApiException(UtilErrorCode.NOT_ALLOWED_QUESTION);
        }
    }
}
