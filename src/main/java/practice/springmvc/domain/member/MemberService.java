package practice.springmvc.domain.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.springmvc.annotation.Trace;
import practice.springmvc.domain.member.repository.jpa.SpringDataJpaMemberRepository;
import practice.springmvc.utils.SHA256Util;

import java.util.Optional;

import static practice.springmvc.utils.SHA256Util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final SpringDataJpaMemberRepository memberRepository;
    @Trace
    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }
    @Trace
    public Member register(Member member) {
        duplicateCheck(member);

        member.setPassword(encryptSHA256(member.getPassword()));

        return memberRepository.save(member);
    }
    @Trace
    public Member login(String loginId, String password) {
        String encryptPassword = encryptSHA256(password);
        return memberRepository.findByLoginIdAndPassword(loginId, encryptPassword);
    }

    @Trace
    public Member getMemberInfo(String loginId) {
        return memberRepository.findByLoginId(loginId).get();
    }

    @Trace
    public boolean isDuplicatedId(String loginId) {
        Optional<Member> optMember = memberRepository.findByLoginId(loginId);
        return !optMember.isEmpty();
    }

    @Trace
    public boolean isDuplicatedNickname(String nickname) {
        Optional<Member> optMember = memberRepository.findByNickname(nickname);
        return !optMember.isEmpty();
    }

    @Trace
    public boolean isDuplicatedEmail(String address) {
        Optional<Member> optMember = memberRepository.findByAddress(address);
        return !optMember.isEmpty();
    }

    @Trace
    public void updatePassword(String loginId, String beforePwd, String afterPwd) {
        String encryptPwd = encryptSHA256(beforePwd);
        Member member = memberRepository.findByLoginIdAndPassword(loginId, encryptPwd);

        if (member != null) {
            member.setPassword(encryptSHA256(afterPwd));
        } else {
            log.error("updatePassword Error {}", member);
            throw new RuntimeException("updatePassword Error 패스워드 변경 메서드 확인이 필요합니다. info : " + member);
        }
    }

    @Trace
    public void deleteId(String loginId, String password) {
        String encryptPwd = encryptSHA256(password);
        Member member = memberRepository.findByLoginIdAndPassword(loginId, encryptPwd);

        if (member != null) {
            memberRepository.deleteById(member.getId());
        } else {
            log.error("deleteId Error {}", member);
            throw new RuntimeException("deleteId Error id 삭제 메서드 확인이 필요합니다. info : " + member);
        }
    }

    private void duplicateCheck(Member member) {
        boolean isDuplicateId = isDuplicatedId(member.getLoginId());
        if (isDuplicateId) {
            throw new RuntimeException("중복된 ID입니다.");
        }

        boolean isDuplicateNickname = isDuplicatedNickname(member.getNickname());
        if (isDuplicateNickname) {
            throw new RuntimeException("중복된 Nickname입니다.");
        }

        boolean isDuplicateEmail = isDuplicatedEmail(member.getAddress());
        if (isDuplicateEmail) {
            throw new RuntimeException("중복된 Email입니다.");
        }
    }
}
