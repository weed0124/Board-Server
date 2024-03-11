package practice.springmvc.domain.member.repository.jpa;


import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import practice.springmvc.domain.member.Member;
import practice.springmvc.domain.member.repository.MemberRepository;

import java.util.Optional;

@Slf4j
//@Repository
@Transactional
public class JpaMemberRepositoryV1 implements MemberRepository {

    private final EntityManager em;

    public JpaMemberRepositoryV1(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }
}
