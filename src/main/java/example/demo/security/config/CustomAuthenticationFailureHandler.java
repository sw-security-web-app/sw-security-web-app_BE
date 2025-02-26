package example.demo.security.config;

import example.demo.data.RedisCustomService;
import example.demo.domain.member.repository.MemberRepository;
import example.demo.error.RestApiException;
import example.demo.security.auth.AuthErrorCode;
import example.demo.security.auth.CustomMemberDetailService;
import example.demo.security.auth.CustomMemberDetails;
import example.demo.security.auth.dto.CustomMemberInfoDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Slf4j(topic = "FAILURE_HANDLER")
@AllArgsConstructor
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    ///최대 실패 횟수
    private static final int MAX_FAILED_ATTEMPTS=5;

    //잠금 시간(30분)
    private static final long LOCK_TIME=1800000;
    //로그인 시도 시간(10분)
    private static final long ATTEMPT_TIME=600000;
    private final RedisCustomService redisCustomService;
    private final String PREFIX="login :";


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String userEmail=request.getParameter("email");

        /*
        * 유저의 락 정보는 로그인 서비스단에서 조회
        * failureHandler는 로그인 실패 시 실패 횟수 추적만 하면됨
        * */
        //Redis에 값이 존재하는 경우
        if(redisCustomService.hasKey(PREFIX+userEmail)){
            int failedAttempts= Integer.parseInt(redisCustomService.getRedisData(PREFIX+userEmail))+1;
            //5회 초과시
            if (failedAttempts>MAX_FAILED_ATTEMPTS){
                //Lock 시간 설정
                redisCustomService.saveRedisData(PREFIX+userEmail, String.valueOf(failedAttempts),LOCK_TIME);
                //로그인 횟수 초과 시 예외
                throw new RestApiException(AuthErrorCode.EXCEED_LOGIN);
            }

            //5회 초과 미만
            long time=redisCustomService.getRemainingTime(PREFIX+userEmail);

            //TTL 만료 시
            if(time<=0){
                redisCustomService.saveRedisData(PREFIX+userEmail,"1",ATTEMPT_TIME);
            }else {
                //이전 값 삭제
                redisCustomService.deleteRedisData(PREFIX+userEmail);
                redisCustomService.saveRedisData(PREFIX+userEmail,String.valueOf(failedAttempts),time);
            }
        }else {
            //Redis에 값이 존재하지 않는 경우
            redisCustomService.saveRedisData(PREFIX+userEmail,"1",ATTEMPT_TIME);
        }
    }
}
