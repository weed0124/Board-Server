package practice.springmvc.web.member.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import practice.springmvc.dto.request.MemberDeleteRequest;
import practice.springmvc.dto.request.MemberLoginRequest;
import practice.springmvc.dto.request.MemberUpdatePwdRequest;
import practice.springmvc.dto.response.LoginResponse;
import practice.springmvc.domain.member.Member;
import practice.springmvc.domain.member.MemberService;
import practice.springmvc.dto.MemberDTO;
import practice.springmvc.dto.response.ResultResponse;
import practice.springmvc.utils.SessionUtil;

import java.net.URI;

import static practice.springmvc.utils.SessionUtil.*;

@Slf4j
@RestController
@RequestMapping("/api/member")
public class MemberApiController {

    private final ResponseEntity<ResultResponse> FAIL_RESPONSE = new ResponseEntity<ResultResponse>(HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<ResultResponse> login(@RequestBody MemberLoginRequest loginRequest, HttpSession session) {
        ResponseEntity<ResultResponse> result = null;

        String loginId = loginRequest.getLoginId();
        String password = loginRequest.getPassword();
        Member loginedMember = memberService.login(loginId, password);
        MemberDTO memberDTO = new MemberDTO(loginedMember);

        if (loginedMember == null) {
            result = ResponseEntity.notFound().build();
        } else if (loginedMember != null) {
            loginResponse = LoginResponse.success(memberDTO);
            setLoginMemberId(session, loginId);

            result = ResponseEntity.ok(new ResultResponse(loginResponse));
        } else {
            throw new RuntimeException("Login Error");
        }

        return result;
    }

    @PostMapping("/logout")
    public void logout(HttpSession session) {
        clear(session);
    }

    @GetMapping("/my-info")
    public ResponseEntity<ResultResponse> memberInfo(HttpSession session) {
        String loginMemberId = getLoginMemberId(session);
        Member memberInfo = memberService.getMemberInfo(loginMemberId);
        loginResponse = LoginResponse.success(new MemberDTO(memberInfo));
        return ResponseEntity.ok(new ResultResponse(loginResponse));
    }

    @PutMapping("password")
    public ResponseEntity<ResultResponse> updatePassword(@RequestBody MemberUpdatePwdRequest request, HttpSession session) {
        ResponseEntity<ResultResponse> result = null;
        String loginMemberId = getLoginMemberId(session);
        try {
            memberService.updatePassword(loginMemberId, request.getBeforePassword(), request.getAfterPassword());
            result = ResponseEntity.ok(new ResultResponse(loginResponse));
        } catch (IllegalArgumentException e) {
            log.error("updatePassword Fail {}", e.getMessage());
            result = FAIL_RESPONSE;
        }

        return result;
    }

    @DeleteMapping
    public ResponseEntity<ResultResponse> deleteId(@RequestBody MemberDeleteRequest request, HttpSession session) {
        ResponseEntity<ResultResponse> result = null;
        String loginMemberId = getLoginMemberId(session);

        try {
            Member member = memberService.login(loginMemberId, request.getPassword());
            memberService.deleteId(loginMemberId, request.getPassword());
            loginResponse = LoginResponse.success(new MemberDTO(member));
            result = ResponseEntity.ok(new ResultResponse(loginResponse));
        } catch (RuntimeException e) {
            log.info("deleteID FAIL");
            result = FAIL_RESPONSE;
        }
        return result;
    }
}