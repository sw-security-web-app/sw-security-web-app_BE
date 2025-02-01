package example.demo.domain.member;

import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface MemberCustomRepository {
    //이메일 중복검사를 위한 이메일 존재 확인
    Long getSameEmailCount(String email);
}
