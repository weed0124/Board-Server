package practice.springmvc.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import practice.springmvc.domain.member.Member;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
    @NotBlank
    String loginId;
    @NotBlank
    String password;
    @NotBlank
    String nickname;
    @NotBlank
    String address;
    Member.Status status;

    public MemberDTO(Member member) {
        this.loginId = member.getLoginId();
        this.password = member.getPassword();
        this.nickname = member.getNickname();
        this.address = member.getAddress();
        this.status = member.getStatus();
    }
}