package practice.springmvc.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import practice.springmvc.dto.MemberDTO;

@Getter
public class LoginResponse {
    enum LoginStatus {
        SUCCESS, FAIL, DELETED
    }

    @NotBlank
    private LoginStatus result;
    private MemberDTO memberDTO;

    public LoginResponse(LoginStatus result) {
        this.result = result;
    }

    public LoginResponse(LoginStatus result, MemberDTO memberDTO) {
        this.result = result;
        this.memberDTO = memberDTO;
    }

    private static final LoginResponse FAIL = new LoginResponse(LoginStatus.FAIL);

    public static LoginResponse success(MemberDTO memberDTO) {
        return new LoginResponse(LoginStatus.SUCCESS, memberDTO);
    }
}
