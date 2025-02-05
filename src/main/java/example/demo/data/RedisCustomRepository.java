package example.demo.data;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RedisCustomRepository extends CrudRepository<RedisCustom,String> {
    //key로 데이터 찾기
    Optional<RedisCustom> findByKeyName(String keyName);
    //value로 데이터 찾기
    Optional<RedisCustom> findByValue(String value);
}
