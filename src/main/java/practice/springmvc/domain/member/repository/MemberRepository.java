package practice.springmvc.domain.member.repository;

import practice.springmvc.annotation.Trace;
import practice.springmvc.domain.member.Member;

import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    @Trace
    Optional<Member> findById(Long id);
}
