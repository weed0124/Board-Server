package practice.springmvc.domain.member.repository;

import practice.springmvc.domain.member.Member;

public interface MemberRepository {

    Member save(Member member);

    Member findById(String id);
}
