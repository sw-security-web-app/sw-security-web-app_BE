package example.demo.security.config;

import example.demo.domain.member.MemberStatus;
import example.demo.security.auth.CustomMemberDetailService;
import example.demo.security.util.JwtAuthFilter;
import example.demo.security.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true,prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig {
    private final CustomMemberDetailService customMemberDetailService;
    private final JwtUtil jwtUtil;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    private static final String[] AUTH_WHITELIST={
            "/api/login","/swagger-ui/**","/api-docs", "/swagger-ui-custom.html",
            "/v3/api-docs/**", "/api-docs/**", "/swagger-ui.html","/api/signup","/api/mail-send",
            "/api/mail-check","/api/send-password","/api/sms-certification/send","/api/sms-certification/confirm",
            "/api/find-email","/api/**"
    };
  /*
  TODO:White List /api/** 삭제
   */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        //CSRF,CORS
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
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
                //회사 관리자만 직원 삭제 가능
                .requestMatchers(HttpMethod.DELETE,"/user").hasRole(MemberStatus.MANAGER.getText())
                .anyRequest().authenticated());

        return http.build();
    }

    //CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration=new CorsConfiguration();
        corsConfiguration.setAllowedOriginPatterns(Arrays.asList("http://localhost:*","http://172.20.10.2:5173"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST","DELETE","OPTIONS","PUT"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",corsConfiguration);
        return source;
    }


}
