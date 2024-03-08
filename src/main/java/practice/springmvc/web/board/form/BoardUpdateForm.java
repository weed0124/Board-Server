package practice.springmvc.web.board.form;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardUpdateForm {
    private String title;
    private String content;
    private String nickname;
    private String password;
    private String ip;
}
