package example.demo.security;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController { // 상태체크를 위한 Controller

    @GetMapping("/health-check")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok("ok");
    }
}
