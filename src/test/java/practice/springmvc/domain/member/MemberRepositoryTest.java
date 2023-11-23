package practice.springmvc.domain.member;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import practice.springmvc.domain.member.repository.MemberRepository;
import practice.springmvc.domain.member.repository.memory.MemoryMemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @AfterEach
    void afterEach() {
        if (memberRepository instanceof MemoryMemberRepository) {
            ((MemoryMemberRepository) memberRepository).clearStore();
        }
    }

    @Test
    public void save() throws Exception {
        // given
        Member member = new Member("익명", "test", "127.0.0.1");

        // when
        Member saveMember = memberRepository.save(member);

        // then
        Member findMember = memberRepository.findById(saveMember.getId()).get();
        assertThat(findMember).isEqualTo(saveMember);
    }
}