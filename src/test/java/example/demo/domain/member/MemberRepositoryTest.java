package example.demo.domain.member;

import example.demo.domain.member.dto.request.MemberRequestDto;
import example.demo.error.RestApiException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("유저의 이메일과 휴대폰번호로 유저아이디를 찾습니다.")
    void findByEmailAndPhoneNumber() {
        //given
        Member member1=Member.createGeneral(
                MemberRequestDto.ofGeneral("tkv00@naver.com","김도연","rlaehdsus!!2","01050299999","GENERAL")
        );

        //when
        memberRepository.save(member1);
        Member findMember1=memberRepository.findByEmailAndPhoneNumber(member1.getEmail(), member1.getPhoneNumber())
                .orElseThrow(()->new RestApiException(MemberErrorCode.INVALID_MAIL_NUMBER));

        //then
        Member findMember2=memberRepository.findById(findMember1.getMemberId())
                .orElseThrow(() -> new RestApiException(MemberErrorCode.INVALID_MAIL_NUMBER));
        assertThat(findMember1).isEqualTo(findMember2);
    }
}