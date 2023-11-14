package practice.springmvc.web.board.form;

import lombok.Getter;
import lombok.Setter;
import practice.springmvc.domain.board.Comment;

import java.util.Date;
import java.util.List;

@Getter @Setter
public class BoardUpdateForm {
    private String title;
    private String content;
    private String writer;
    private String password;
}
