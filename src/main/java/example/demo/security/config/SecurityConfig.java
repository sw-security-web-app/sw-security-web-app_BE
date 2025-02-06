package example.demo.security.config;

import example.demo.security.auth.CustomMemberDetailService;
import example.demo.security.util.JwtAuthFilter;
import example.demo.security.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig {
    private final CustomMemberDetailService customMemberDetailService;
    private final JwtUtil jwtUtil;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    private static final String[] AUTH_WHITELIST={
            "/api/login","/swagger-ui/**","/api-docs", "/swagger-ui-custom.html",
            "/v3/api-docs/**", "/api-docs/**", "/swagger-ui.html","/api/signup"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        //CSRF,CORS
        http.csrf((csrf)->csrf.disable());
        http.cors((Customizer.withDefaults()));

        //세션 관리 상태 없음 구성,
        http.sessionManagement(sessionManagement->sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS
        ));

        //FormLogin,BasicHttp 비활성화
        http.formLogin((form)->form.disable());
        http.httpBasic(AbstractHttpConfigurer::disable);

        //JWT Filter를 UsernamePasswordAuthenticationFilter 앞에 추가
        http.addFilterBefore(new JwtAuthFilter(customMemberDetailService,jwtUtil), UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling((exceptionHandling)->exceptionHandling
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler));

        //권한 규칙 생성
        http.authorizeHttpRequests(authorize->authorize
                .requestMatchers(AUTH_WHITELIST).permitAll()
                //@PreAuthorization을 사용
                .anyRequest().permitAll());

        return http.build();
    }


}
