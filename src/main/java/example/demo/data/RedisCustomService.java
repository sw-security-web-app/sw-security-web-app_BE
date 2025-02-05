package example.demo.data;

public interface RedisCustomService {
    void saveRedisData(String keyName, String value, Long limitTime) ;
    String getRedisData(String key);
    void deleteRedisData(String key);
    boolean hasKey(String key);
}
