package practice.springmvc.domain.board.repository.jpa;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import practice.springmvc.domain.board.Board;
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
        return em.createQuery(jpql, Board.class).getResultList();
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
