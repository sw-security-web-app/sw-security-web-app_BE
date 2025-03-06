package example.demo.verification.util;

import example.demo.error.RestApiException;
import example.demo.util.UtilErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.web.reactive.function.client.WebClient.*;

@Component
public class PythonServerUtil {

    private final WebClient webClient;

    @Value("${python.server.url}")
    private String pythonServerUrl;

    public PythonServerUtil(Builder webClient) {
        this.webClient = webClient.build();
    }

    public void validatePrompt(String prompt) {
        Map<String, String> requestBody = Map.of("prompt", prompt);

        HttpStatusCode responseStatus = webClient.post()
                .uri(pythonServerUrl + "/predict")
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .exchangeToMono(response -> Mono.just(response.statusCode()))
                .block();

        if (responseStatus == null) {
            throw new RestApiException(UtilErrorCode.PYTHON_SERVER_ERROR);
        } else if (!responseStatus.is2xxSuccessful()) {
            throw new RestApiException(UtilErrorCode.PYTHON_SERVER_ERROR);
        }
    }
}
