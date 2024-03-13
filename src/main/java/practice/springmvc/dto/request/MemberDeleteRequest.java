package practice.springmvc.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDeleteRequest {
    @NotBlank
    private String id;
    @NotBlank
    private String password;
}
