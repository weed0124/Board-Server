package practice.springmvc.domain.member.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.springmvc.domain.member.Member;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member, String> {
}
