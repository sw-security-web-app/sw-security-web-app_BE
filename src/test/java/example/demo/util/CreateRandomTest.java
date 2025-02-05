package example.demo.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CreateRandomTest {
    @Test
    @DisplayName("UUID 암호가 생성되는지 확인하고 8자리와 중복되지 않는지 확인합니다.")
    void createUuid(){
        //given//when
        String uuid1=CreateRandom.createShortUuid();
        String uuid2=CreateRandom.createShortUuid();

        //then

        //1, uuid가 null이 아닌지 확인
        assertThat(uuid1).isNotEmpty();
        assertThat(uuid2).isNotEmpty();

        //2. uuid의 길이가 8자리인지 확인
        assertThat(uuid1.length()).isEqualTo(8);
        assertThat(uuid2.length()).isEqualTo(8);

        //3. uuid가 다른 값인지 확인
        assertThat(uuid1).isNotEqualTo(uuid2);

    }

    @Test
    @DisplayName("6자리의 랜덤한 숫자가 생성됩니다.")
    void createRandomNumber(){
        //given //when
        String random1=CreateRandom.createRandomNumber();
        String random2=CreateRandom.createRandomNumber();

        //then
        assertThat(random1.length()).isEqualTo(6);
        assertThat(random2.length()).isEqualTo(6);

    }

}