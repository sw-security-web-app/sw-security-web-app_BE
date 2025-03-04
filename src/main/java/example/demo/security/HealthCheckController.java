package example.demo.security;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/health-check/")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok("ok");
    }
}
