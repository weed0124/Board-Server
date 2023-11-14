package practice.springmvc.web.board.form;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardSaveForm {

    private String title;
    private String content;
    private String writer;
    private String password;
}
