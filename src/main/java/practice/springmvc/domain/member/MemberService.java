package practice.springmvc.domain.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import practice.springmvc.annotation.Trace;
import practice.springmvc.domain.member.repository.MemberRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Trace
    public Member save(Member member) {
        return memberRepository.save(member);
    }

    @Trace
    public Optional<Member> findById(String id) {
        return memberRepository.findById(id);
    }

}
