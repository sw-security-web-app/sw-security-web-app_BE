package example.demo.domain.member.repository;

import example.demo.domain.member.Member;
import example.demo.domain.member.dto.request.MemberRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;
    @AfterEach
    void teardown(){
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("이메일과 휴대폰 번호로 유저를 찾습니다.")
    @Test
    void findByUserPhoneNumberAndEmail(){
       //given
        MemberRequestDto general=MemberRequestDto.ofGeneral(
                "tkv99@naver.com", "김김김", "rlaehdus00!!", "01050299737","GENERAL"
        );
        Member member=Member.createGeneral(general);
        memberRepository.save(member);

        //when
        Member findMember=memberRepository.findByEmailAndPhoneNumber(member.getEmail(),member.getPhoneNumber()).get();

        //then
        isSameMember(findMember, member);
    }

    @DisplayName("이메일로 유저를 찾습니다.")
    @Test
    void findByUserEmail(){
        //given
        MemberRequestDto general=MemberRequestDto.ofGeneral(
                "tkv99123@naver.com", "김김김", "rlaehdus00!!", "01050299737","GENERAL"
        );
        Member member=Member.createGeneral(general);
        memberRepository.save(member);

        //when
        Member findMember=memberRepository.findByEmail(general.getEmail()).get();

        //then
        isSameMember(findMember, member);
    }
    @DisplayName("휴대폰 번호로 유저를 찾습니다.")
    @Test
    void findByPhoneNumber(){
        //given
        MemberRequestDto general=MemberRequestDto.ofGeneral(
                "tkv99123@naver.com", "김김김", "rlaehdus00!!", "01012345678","GENERAL"
        );
        Member member=Member.createGeneral(general);
        memberRepository.save(member);

        //when
        Member findMember=memberRepository.findByPhoneNumber(member.getPhoneNumber()).get();
        //then
        isSameMember(findMember,member);
    }

    private  void isSameMember(Member findMember, Member member) {
        assertThat(findMember.getPhoneNumber())
                .isEqualTo(member.getPhoneNumber());
        assertThat(findMember.getEmail())
                .isEqualTo(member.getEmail());
        assertThat(findMember.getMemberStatus())
                .isEqualTo(member.getMemberStatus());
        assertThat(findMember.getUserName())
                .isEqualTo(member.getUserName());
        assertThat(findMember.getPassword())
                .isEqualTo(member.getPassword());
    }

}