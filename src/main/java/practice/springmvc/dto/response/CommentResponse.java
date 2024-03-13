package practice.springmvc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import practice.springmvc.domain.board.comment.Comment;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private Long id;
    private String content;
    private String nickname;
    private String password;
    private String ip;
    private Comment parent;
    private List<Comment> children = new ArrayList<>();

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.nickname = comment.getNickname();
        this.password = comment.getPassword();
        this.ip = comment.getIp();
        this.parent = comment.getParent();
        this.children = comment.getChildren();
    }
}
