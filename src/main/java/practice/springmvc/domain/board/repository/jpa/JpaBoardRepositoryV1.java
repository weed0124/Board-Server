package practice.springmvc.domain.board.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import practice.springmvc.domain.board.Board;
import practice.springmvc.domain.board.BoardSearchCond;
import practice.springmvc.domain.board.repository.BoardRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@Transactional
public class JpaBoardRepositoryV1 implements BoardRepository {

    private final EntityManager em;

    public JpaBoardRepositoryV1(EntityManager em) {
        this.em = em;
    }

    @Override
    public Board save(Board board) {
        em.persist(board);
        return board;
    }

    @Override
    public Optional<Board> findById(Long id) {
        Board findBoard = em.find(Board.class, id);
        return Optional.ofNullable(findBoard);
    }

    @Override
    public List<Board> findAll() {
        String jpql = "select b from Board b";
        TypedQuery<Board> query = em.createQuery(jpql, Board.class);
        return query.getResultList();
    }

    @Override
    public List<Board> findAll(BoardSearchCond cond) {
        String title = cond.getTitle();
        String nickname = cond.getNickname();

        String jpql = "select b from Board b, Member m where b.memberId = m.id";

        if (StringUtils.hasText(title)) {
            jpql += " and b.title like concat('%', :title, '%')";
        }

        if (StringUtils.hasText(nickname)) {
            jpql += " and m.nickname like concat('%', :nickname, '%')";
        }

        TypedQuery<Board> query = em.createQuery(jpql, Board.class);
        if (StringUtils.hasText(title)) {
            query.setParameter("title", title);
        }

        if (StringUtils.hasText(nickname)) {
            query.setParameter("nickname", nickname);
        }

        return query.getResultList();
    }

    @Override
    public void update(Long boardId, Board updateParam) {
        Board findBoard = em.find(Board.class, boardId);
        findBoard.setTitle(updateParam.getTitle());
        findBoard.setContent(updateParam.getContent());
        findBoard.setUpdateDate(LocalDateTime.now());
    }

    @Override
    public void addReadCount(Long boardId) {
        Board findBoard = em.find(Board.class, boardId);
        findBoard.setReadCount(findBoard.getReadCount() + 1);
    }
}
