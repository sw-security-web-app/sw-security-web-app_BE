package example.demo.data;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service

public class RedisCustomServiceImpl implements RedisCustomService{
    private final RedisTemplate<String,Object> redisTemplate;

    public RedisCustomServiceImpl(RedisTemplate<String,Object> redisTemplate){
        this.redisTemplate=redisTemplate;
    }

    @Override
    public void saveRedisData(String keyName, String value, Long limitTime) {
        redisTemplate.opsForValue().set(keyName,value,limitTime, TimeUnit.SECONDS);
    }

    @Override
    public String getRedisData(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    @Override
    public void deleteRedisData(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
}
