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
}