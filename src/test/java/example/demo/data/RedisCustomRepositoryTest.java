package example.demo.data;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class RedisCustomRepositoryTest {
    @Autowired
    private RedisCustomRepository redisCustomRepository;

    @Test
    @DisplayName("저장한 데이터를 key이름으로  데이터를 찾는다")
    void searchRedis(){
        //given
        RedisCustom test1=RedisCustom.builder()
                .keyName("testKey1")
                .value("testValue1")
                .TTL(3*60)
                .build();
        redisCustomRepository.save(test1);
        //when
        Optional<RedisCustom> findByKeyName = redisCustomRepository.findByKeyName(test1.getKeyName());


        //then
        assertThat(test1.getKeyName())
                .isEqualTo(findByKeyName.orElseThrow().getKeyName());
        assertThat(test1.getValue())
                .isEqualTo(findByKeyName.orElseThrow().getValue());
        assertThat(test1.getTTL())
                .isEqualTo(findByKeyName.orElseThrow().getTTL());

    }
}