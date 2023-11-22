package practice.springmvc.domain.member.repository.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import practice.springmvc.domain.member.Member;
import practice.springmvc.domain.member.repository.MemberRepository;

import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class JpaMemberRepositoryV2 implements MemberRepository {

    private final SpringDataJpaMemberRepository repository;

    @Override
    public Member save(Member member) {
        return repository.save(member);
    }

    @Override
    public Optional<Member> findById(String id) {
        return repository.findById(id);
    }
}
