package example.demo.data;

import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@DisplayName("Redis Test Containers")
@ActiveProfiles("test")
@TestConfiguration
public class RedisTestContainers {
    private static final String REDIS_DOCKER_IMAGE="redis:5.0.3-alpine";

    static {
        GenericContainer<?> REDIS_CONTAINER=
                new GenericContainer<>(DockerImageName.parse(REDIS_DOCKER_IMAGE))
                        .withExposedPorts(6379)
                        .withReuse(true);

        REDIS_CONTAINER.start();

        System.setProperty("spring.data.redis.host",REDIS_CONTAINER.getHost());
        System.setProperty("spring.data.redis.port",REDIS_CONTAINER.getMappedPort(6379).toString());
    }
}
