package example.demo.security.domain;

import example.demo.data.RedisCustomService;
import example.demo.error.RestApiException;
import example.demo.security.exception.SecurityErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;

import java.util.Optional;
import java.util.Set;


@RequiredArgsConstructor
public class RefreshToken {
    private  final RedisCustomService redisCustomService;

    //Refresh Token 설정 시간 - 3일
    private static final Long REFRESH_EXPIRATION_TIME=259200000L;

    //리프레쉬 토큰 가져오기
    public String getRefreshToken(final String refreshToken){
        return Optional.ofNullable(redisCustomService.getRedisData(refreshToken))
                .orElseThrow(()->new RestApiException(SecurityErrorCode.NOT_EXIST_REFRESH_TOKEN));
    }
    //리프레쉬 토큰 저장
    //id : 유저 id값
    public void putRefreshToken(final String refreshToken,Long id){
        redisCustomService.saveRedisData("refresh "+id,refreshToken,REFRESH_EXPIRATION_TIME);
    }

    //리프레쉬 토큰 삭제
    public void removeRefreshToken(final Long id){
        redisCustomService.deleteRedisData("refresh "+id);
    }

    //유저의 리프레쉬 토큰 삭제
    public void removeUserRefreshToken(final Long id){
        Set<String> keys=redisCustomService.getKeysByPattern("refresh *");
        for (String key:keys){
            String storedId=redisCustomService.getRedisData(key);
            if(storedId!=null && storedId.equals("refresh "+id)){
                redisCustomService.deleteRedisData(storedId);
            }
        }
    }

}
