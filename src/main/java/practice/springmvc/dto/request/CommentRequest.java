package practice.springmvc.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.board.comment.Comment;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
    private Long id;
    private String content;
    private String nickname;
    private String password;
    private Long boardId;
    private Long parentId;
}
