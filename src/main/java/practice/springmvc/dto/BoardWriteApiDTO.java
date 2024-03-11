package practice.springmvc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardWriteApiDTO {
    private Long id;
    private String title;
    private String content;
    private String nickname;
    private String password;
    private String ip;
}
