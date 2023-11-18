package practice.springmvc.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class MemberRepository {
    private static final Map<String, Member> store = new ConcurrentHashMap<>();

    public Member save(Member member) {
        member.setId(UUID.randomUUID().toString());
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(String id) {
        return store.get(id);
    }

    public void clearStore() {
        store.clear();
    }
}
