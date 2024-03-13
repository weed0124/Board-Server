package practice.springmvc.domain.member.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import practice.springmvc.domain.member.Member;

import java.util.Optional;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long> {

//    @Query("select m from Member m where m.loginId = :loginId and m.password = :password")
    Member findByLoginIdAndPassword(@Param("loginId") String loginId, @Param("password") String password);

//    @Query("select m from Member m where m.loginId = :loginId")
    Optional<Member> findByLoginId(String loginId);
    Optional<Member> findByAddress(String address);
    Optional<Member> findByNickname(String nickname);
}
