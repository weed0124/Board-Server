package practice.springmvc.domain.board;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Comment {
    private Long commentId;
    private String register;
    private String password;
    private String content;
}
