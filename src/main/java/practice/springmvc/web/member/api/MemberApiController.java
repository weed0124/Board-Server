package practice.springmvc.web.member.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import practice.springmvc.dto.request.MemberLoginRequest;
import practice.springmvc.dto.response.LoginResponse;
import practice.springmvc.domain.member.Member;
import practice.springmvc.domain.member.MemberService;
import practice.springmvc.dto.MemberDTO;
import practice.springmvc.utils.SessionUtil;

import java.net.URI;

@RestController
@RequestMapping("/api/member")
public class MemberApiController {

    private final MemberService memberService;
    private static LoginResponse loginResponse;

    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity signUp(@RequestBody @Valid MemberDTO memberDTO, HttpServletRequest request) {
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

    @PostMapping("/sign-in")
    public ResponseEntity<LoginResponse> login(@RequestBody MemberLoginRequest loginRequest, HttpSession session) {
        ResponseEntity<LoginResponse> result = null;

        String loginId = loginRequest.getLoginId();
        String password = loginRequest.getPassword();
        Member loginedMember = memberService.login(loginId, password);
        MemberDTO memberDTO = new MemberDTO(loginedMember);

        if (loginedMember == null) {
            return ResponseEntity.notFound().build();
        } else if (loginedMember != null) {
            loginResponse = LoginResponse.success(memberDTO);
            SessionUtil.setLoginMemberId(session, loginId);

            result = new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
        } else {
            throw new RuntimeException("Login Error");
        }

        return result;
    }

    @PostMapping("/logout")
    public void logout(HttpSession session) {
        SessionUtil.clear(session);
    }
}