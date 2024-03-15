package practice.springmvc.domain.board.tag.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import practice.springmvc.domain.board.tag.QTag;
import practice.springmvc.domain.board.tag.Tag;

import java.util.List;

import static practice.springmvc.domain.board.tag.QTag.*;

public class TagRepositoryImpl implements TagJPARepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public TagRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public List<Tag> getAllTagsByBoard(Long boardId) {
        return query.selectFrom(tag)
                .where(tag.board.id.eq(boardId))
                .orderBy(tag.id.asc())
                .fetch();
    }
}
