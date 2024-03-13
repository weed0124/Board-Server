package practice.springmvc.domain.board.comment;

import java.util.List;

public interface CommentJPARepository {
    List<Comment> getAllCommentsByBoard(Long boardId);
}
