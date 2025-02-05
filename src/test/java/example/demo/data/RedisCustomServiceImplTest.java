package example.demo.data;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Rollback(value = false)
class RedisCustomServiceImplTest {
    @Autowired
    private RedisCustomRepository redisCustomRepository;
    @Autowired
    private RedisCustomServiceImpl redisCustomService;

    @Autowired
    private RedisCustom redisCustom;

    @BeforeEach
    void setUp(){
        redisCustom=RedisCustom.builder()
                .keyName("key1")
                .value("value1")
                .build();
    }

//TODO : 시간초과에 따른 키값 삭제 구현필요



}