package practice.springmvc.domain.board.comment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import practice.springmvc.domain.board.QBoard;

import java.util.List;

import static practice.springmvc.domain.board.QBoard.*;
import static practice.springmvc.domain.board.comment.QComment.*;

public class CommentRepositoryImpl implements CommentJPARepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public CommentRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public List<Comment> getAllCommentsByBoard(Long boardId) {
        return query.selectFrom(comment)
                .where(comment.board.id.eq(boardId))
                .orderBy(
                        comment.parent.id.asc().nullsFirst(),
                        comment.createdDate.asc()
                ).fetch();
    }
}
