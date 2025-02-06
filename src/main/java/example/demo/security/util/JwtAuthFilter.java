package example.demo.security.util;

import example.demo.security.auth.CustomMemberDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final CustomMemberDetailService customMemberDetailService;
    private final JwtUtil jwtUtil;

    //JWT 토큰 검증 필터
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader=request.getHeader("Authorization");

        //JWT헤더가 있는 경우
        if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")){
            String token=authorizationHeader.substring(7);
            //JWT 유효성 검증
            if(jwtUtil.validateToken(token)){
                Long memberId=jwtUtil.getMemberId(token);

                //유저와 토큰 일치 시 userDetail생성
                UserDetails userDetails= customMemberDetailService.loadUserByUsername(memberId.toString());

                if(userDetails!=null){
                    //UserDetails, Password,Role -> 접근권한 인증 token생성
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                            new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                    //현재 Request와 Security Context 접근 권한 설정
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }
        filterChain.doFilter(request,response);
    }

}
