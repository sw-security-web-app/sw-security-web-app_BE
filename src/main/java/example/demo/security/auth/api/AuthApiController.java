package example.demo.security.auth.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthApiController {
    private final AuthService authService;

//    @PostMapping("login")
//    public ResponseEntity<?>login(@Validated(ValidationSequence.class) @RequestBody MemberLoginDto loginDto){
//        String token=authService.
//    }
}
