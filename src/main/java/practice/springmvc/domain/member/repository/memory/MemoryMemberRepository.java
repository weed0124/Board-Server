package practice.springmvc.domain.member.repository.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import practice.springmvc.domain.member.Member;
import practice.springmvc.domain.member.repository.MemberRepository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class MemoryMemberRepository implements MemberRepository {
    private static final Map<String, Member> store = new ConcurrentHashMap<>();

    public Member save(Member member) {
        member.setId(UUID.randomUUID().toString());
        store.put(member.getId(), member);
        return member;
    }

    public Optional<Member> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public void clearStore() {
        store.clear();
    }
}
