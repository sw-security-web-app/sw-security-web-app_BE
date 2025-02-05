package example.demo.data;

import lombok.*;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.stereotype.Component;


@RedisHash("redis_custom")
@Getter
@Component
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RedisCustom {
    @Id
    private String keyName;

    @Indexed
    private String value;

    @TimeToLive
    private int TTL;

    @Builder
    public RedisCustom( String keyName, String value,int TTL) {
        this.keyName = keyName;
        this.value = value;
        this.TTL=TTL;
    }


}
