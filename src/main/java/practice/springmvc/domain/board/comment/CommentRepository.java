package practice.springmvc.domain.board.comment;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CommentRepository {

    private static final Map<Long, Comment> store = new ConcurrentHashMap<>();

    private static long sequence = 0L;

    public Comment save(Comment comment) {
        comment.setId(++sequence);
        store.put(comment.getId(), comment);
        return comment;
    }

    public List<Comment> findByBoardId(Long boardId) {
        return new ArrayList<>(store.values()).stream()
                .filter(comment -> comment.getBoardId().equals(boardId))
                .toList();
    }
}
