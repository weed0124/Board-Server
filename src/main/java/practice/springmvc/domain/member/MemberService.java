package practice.springmvc.domain.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import practice.springmvc.annotation.Trace;
import practice.springmvc.domain.member.repository.jpa.SpringDataJpaMemberRepository;
import practice.springmvc.utils.SHA256Util;

import java.util.Optional;

import static practice.springmvc.utils.SHA256Util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final SpringDataJpaMemberRepository memberRepository;
    @Trace
    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }
    @Trace
    public Member register(Member member) {
        boolean isDuplicate = isDuplicatedId(member.getLoginId());
        if (isDuplicate) {
            throw new RuntimeException("중복된 ID입니다.");
        }

        member.setPassword(encryptSHA256(member.getPassword()));

        return memberRepository.save(member);
    }

    public Member login(String loginId, String password) {
        String encryptPassword = encryptSHA256(password);
        return memberRepository.findByLoginIdAndPassword(loginId, encryptPassword);
    }

    public Member getMemberInfo(String loginId) {
        return memberRepository.findByLoginId(loginId).get();
    }

    public boolean isDuplicatedId(String loginId) {
        Optional<Member> optMember = memberRepository.findByLoginId(loginId);
        return !optMember.isEmpty();
    }

    public void updatePassword(String loginId, String beforePwd, String afterPwd) {

    }

    public void deleteId(String loginId, String password) {
        memberRepository.findByLoginIdAndPassword(loginId, password);
    }
}
