package practice.springmvc.web.member.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import practice.springmvc.dto.response.Result;
import practice.springmvc.domain.member.Member;
import practice.springmvc.domain.member.MemberService;
import practice.springmvc.dto.MemberDTO;

import java.net.URI;

@RestController
@RequestMapping("/api/member")
public class MemberApiController {

    private final MemberService memberService;

    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Result> signUp(@RequestBody @Valid MemberDTO memberDTO, HttpServletRequest request) {
        Member member = memberService.register(new Member(memberDTO.getLoginId(),
                memberDTO.getPassword(),
                memberDTO.getNickname(),
                memberDTO.getAddress(),
                memberDTO.getStatus()));

        URI location = ServletUriComponentsBuilder.fromContextPath(request)
                .path("/api/member/{id}")
                .buildAndExpand(member.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}