package example.demo.domain.member.repository;

import example.demo.domain.member.Member;
import example.demo.domain.member.repository.MemberCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long>, MemberCustomRepository {
    Optional<Member> findByEmailAndPhoneNumber(String email, String phoneNumber);
}
