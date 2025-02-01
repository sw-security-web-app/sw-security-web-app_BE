package example.demo.domain.member;

import example.demo.domain.member.dto.request.MemberRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class MemberCustomRepositoryImplTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("이메일 중복 검사를 실시합니다.")
    void isDuplicatedEmail(){
        //given
        Member member1=Member.createGeneral(
                MemberRequestDto.ofGeneral("tkv00@naver.com","김도연","rlaehdsus!!2","01050299999","GENERAL")
        );
        Member member2=Member.createGeneral(
                MemberRequestDto.ofGeneral("tkv11@naver.com","김도연","rlaehdsus!!2","01050299999","GENERAL")
        );
        Member member3=Member.createGeneral(
                MemberRequestDto.ofGeneral("tkv11@naver.com","김도연","rlaehdsus!!2","01050299999","GENERAL")
        );
        memberRepository.saveAll(List.of(member1,member2,member3));

        //when
        Long member1EmailDuplicateState=memberRepository.getSameEmailCount(member1.getEmail());
        Long member23EmailDuplicateState=memberRepository.getSameEmailCount(member2.getEmail());
        Long member4EmailDuplicateState=memberRepository.getSameEmailCount("tk@naver.com");

        //then
        assertThat(member1EmailDuplicateState)
                .isEqualTo(1L);
        assertThat(member23EmailDuplicateState)
                .isEqualTo(2L);
        assertThat(member4EmailDuplicateState)
                .isEqualTo(0L);
    }

    @Test
    @DisplayName("휴대폰 번호 중복 검사를 실시합니다.")
    void isDuplicatedPhoneNumber(){
        //given
        Member member1=Member.createGeneral(
                MemberRequestDto.ofGeneral("tkv00@naver.com","김도연","rlaehdsus!!2","01050299999","GENERAL")
        );
        Member member2=Member.createGeneral(
                MemberRequestDto.ofGeneral("tkv11@naver.com","김도연","rlaehdsus!!2","01050299999","GENERAL")
        );
        Member member3=Member.createGeneral(
                MemberRequestDto.ofGeneral("tkv11@naver.com","김도연","rlaehdsus!!2","01050299911","GENERAL")
        );
        memberRepository.saveAll(List.of(member1,member2,member3));

        //when
        Long member1EmailDuplicateState=memberRepository.getPhoneNumberCount(member1.getPhoneNumber());
        Long member23EmailDuplicateState=memberRepository.getPhoneNumberCount(member3.getPhoneNumber());
        Long member4EmailDuplicateState=memberRepository.getPhoneNumberCount("01011111111");

        //then
        assertThat(member1EmailDuplicateState)
                .isEqualTo(2L);
        assertThat(member23EmailDuplicateState)
                .isEqualTo(1L);
        assertThat(member4EmailDuplicateState)
                .isEqualTo(0L);
    }

}