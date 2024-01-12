package practice.springmvc.domain.board.repository.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.util.StringUtils;
import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.board.BoardSearchCond;

import java.util.List;

import static practice.springmvc.domain.board.QBoard.board;

public class SpringDataJpaBoardRepositoryImpl implements BoardJPARepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public SpringDataJpaBoardRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public List<Board> findBoardList(BoardSearchCond cond) {
        String title = cond.getTitle();
        String nickname = cond.getNickname();

        return query
                .select(board)
                .from(board)
                .where(likeTitle(title), likeNickname(nickname))
                .fetch();
    }

    private BooleanExpression likeTitle(String title) {
        if (StringUtils.hasText(title)) {
            return board.title.like("%" + title + "%");
        }
        return null;
    }

    private BooleanExpression likeNickname(String nickname) {
        if (StringUtils.hasText(nickname)) {
            return board.member.nickname.like("%" + nickname + "%");
        }
        return null;
    }
}
