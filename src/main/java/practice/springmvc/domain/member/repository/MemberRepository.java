package practice.springmvc.domain.member.repository;

import practice.springmvc.domain.member.Member;

import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    Optional<Member> findById(String id);
}
