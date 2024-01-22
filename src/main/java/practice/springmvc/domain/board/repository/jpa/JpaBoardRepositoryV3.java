package practice.springmvc.domain.board.repository.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import practice.springmvc.annotation.Trace;
import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.board.BoardSearchCond;
import practice.springmvc.domain.board.QBoard;
import practice.springmvc.domain.board.repository.BoardRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static practice.springmvc.domain.board.QBoard.board;

@Slf4j
//@Repository
@Transactional
public class JpaBoardRepositoryV3 implements BoardRepository {

    private final SpringDataJpaBoardRepository repository;
    private final JPAQueryFactory query;

    public JpaBoardRepositoryV3(SpringDataJpaBoardRepository repository, EntityManager em) {
        this.repository = repository;
        this.query = new JPAQueryFactory(em);
    }

    @Trace
    @Override
    public Board save(Board board) {
        return repository.save(board);
    }

    @Trace
    @Override
    public Optional<Board> findById(Long id) {
        return repository.findById(id);
    }

    @Trace
    @Override
    public List<Board> findAll(BoardSearchCond cond) {
        String nickname = cond.getNickname();
        String title = cond.getTitle();

        return query
                .select(board)
                .from(board)
                .where(likeNickname(nickname), likeTitle(title))
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

    @Trace
    @Override
    public void update(Long boardId, Board updateParam) {
        Board findBoard = findById(boardId).orElseThrow();
        findBoard.setTitle(updateParam.getTitle());
        findBoard.setContent(updateParam.getContent());
    }

    @Override
    public void addReadCount(Long boardId) {
        Board findBoard = findById(boardId).orElseThrow();
        findBoard.setReadCount(findBoard.getReadCount() + 1);
    }
}
