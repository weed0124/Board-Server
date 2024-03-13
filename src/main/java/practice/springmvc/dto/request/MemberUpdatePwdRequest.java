package practice.springmvc.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdatePwdRequest {
    @NotBlank
    private String beforePassword;

    @NotBlank
    private String afterPassword;
}
