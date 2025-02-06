package example.demo.config;

import example.demo.security.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.security.util")
public class CommonConfiguration {
    @Value("${spring.jwt.secret}")
    private String secretKey;

    @Value("${spring.jwt.expiration_time}")
    private long accessTokenExpTime;

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil(secretKey, accessTokenExpTime);
    }
}
